package org.example.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.interact.dto.FeignCommentDto;
import org.example.interact.dto.UserDto;
import org.example.interact.mapper.CommentMapper;
import org.example.interact.entity.CommentEntity;
import org.example.interact.feign.BasicFeignService;
import org.example.interact.service.CommentService;
import org.example.interact.utils.ThreadPool;
import org.example.interact.dto.CommentDto;
import org.example.interact.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author carey
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Integer countByVideoId(Long videoId) {
        return this.count(new QueryWrapper<CommentEntity>().eq("video_id", videoId));
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "comment", key = "#videoId")
    @Override
    public CommentDto postComment(long userId, long videoId, int actionType, String commentText, Long commentId,String token) {
        // 获取视频作者id
        UserInfoVo authorInfoVo=basicFeignService.getUserByVideoId(videoId,token);
        long id=authorInfoVo.getUserDto().getUserId();
        // 获取当前用户信息
        UserInfoVo userInfoVo = basicFeignService.getUserById(userId,token);
//         当前userId不存在user
        UserDto userDto=userInfoVo.getUserDto();

        if (userDto == null){
            return null;
        }
        CommentEntity commentEntity = new CommentEntity();

        // actionType==1添加评论，否则删除评论
        if (actionType == 1) {
            commentEntity.setAuthorId( userDto.getUserId());
            commentEntity.setVideoId(videoId);
            commentEntity.setContent(commentText);
            commentEntity.setCreatedTime(new Date());
            this.save(commentEntity);
            FeignCommentDto feignCommentDto=new FeignCommentDto(commentEntity,token);
//            // 加入kafka消息队列 topic为commentQueue
            kafkaTemplate.send("commentQueue",JSON.toJSONString(feignCommentDto));
        } else if (actionType==2) {
            commentEntity = this.getById(commentId);
            // 若评论存在且是当前用户发布的，才可以删除
            if (commentEntity != null && commentEntity.getAuthorId().equals(userDto.getUserId())) {
                this.removeById(commentId);
                FeignCommentDto feignCommentDto=new FeignCommentDto(commentEntity,token);
                // 加入kafka消息队列 topic为 removeCommentQueue
                kafkaTemplate.send("removeCommentQueue", JSON.toJSONString(feignCommentDto));
            }
            // 若当前评论不存在则删除出错
            else {
                return null;
            }
        }
        // actionType不为1或2
        else {
            return null;
        }

        CommentDto comment = new CommentDto();
        comment.setId(commentEntity.getId());
        comment.setUser(userDto);
        comment.setContent(commentEntity.getContent());
        comment.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentEntity.getCreatedTime()));
        return comment;
    }


    @Cacheable(value = "comment", key = "#videoId", sync = true)
    @Override
    public CommentDto[] getCommentList(long userId, long videoId) throws Exception {
        List<CommentEntity> comments = baseMapper.selectList(new QueryWrapper<CommentEntity>().eq("video_id", videoId));
        CommentDto[] commentList = new CommentDto[comments.size()];
        //使用线程池对评论进行并行处理
        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            int finalI = i;
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                //CompletableFuture.runAsync() 创建异步任务。supplyAsync() 用于有返回值的任务，而 runAsync() 用于没有返回值的任务。
                CommentDto comment = new CommentDto();
                CommentEntity commentEntity = comments.get(finalI);
                String token="11";
                UserInfoVo userInfoVo = basicFeignService.getUserById(commentEntity.getAuthorId(),token);
                UserDto author=userInfoVo.getUserDto();

                comment.setId(commentEntity.getId());
                comment.setContent(commentEntity.getContent());
                comment.setUser(author);
                comment.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentEntity.getCreatedTime()));

                commentList[finalI] = comment;
            }, ThreadPool.executor);
            futureList.add(future);
        }

        // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return commentList;
    }
}
