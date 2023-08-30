DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户id 自动生成' ,
  `name` varchar(32) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '随用户注册上传',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码通过md5	加密后存储',
	`avatar` varchar(255) COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像可以更换——默认为初始照片上传到服务器中',
	`backgroud_image` varchar(255) COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '背景图片可以更换——默认为初始照片上传到服务器中',
	`sinature` varchar(255) COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个性签名--默认为空字符串可以更改',
	`follow_count` int(0) NULL DEFAULT NULL COMMENT '关注总数',
  `follower_count` int(0) NULL DEFAULT NULL COMMENT '粉丝总数',
	`total_favorited` int(0) NULL DEFAULT NULL COMMENT '获赞总数',
	`work_count` int(0) NULL DEFAULT NULL COMMENT '作品总数',
	`favorite_count` int(0) NULL DEFAULT NULL COMMENT '获赞总数',
	
	
  PRIMARY KEY (`user_id`),
	UNIQUE(`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

#
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
    `video_id` BIGINT(20) NOT  NULL AUTO_INCREMENT COMMENT '视频id 自动生成',
    `author_id` BIGINT(20) not NULL COMMENT '作者id 创建时token携带',
    `title` varchar(20) COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT'创建时携带',
    `play_url` varchar(255) COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频地址——必须在创建时一同上传？这俩接口没给',
		`cover_url` varchar(255) COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面地址——必须在创建时一同上传？这俩接口没给请求路径',
		`favorite_count` int(0) NULL DEFAULT NULL COMMENT '获赞总数',
		`comment_count` int(0) NULL DEFAULT NULL COMMENT '评论总数',
    `created_time` datetime not NULL,
    PRIMARY KEY (`video_id`),
		INDEX(author_id) COMMENT '用户id用于实现发布列表统计userDto发布视频数量'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论id自动生成',
    `video_id` BIGINT(20) NOT NULL ,
    `author_id` BIGINT(20) NOT NULL COMMENT '评论者id',
    `content` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_time` datetime not NULL,
    PRIMARY KEY (`id`),
   INDEX(video_id) COMMENT '用于统计favorite count和实现评论列表操作',
	 INDEX(author_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `favor`;
CREATE TABLE `favor` (
		 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,   
    `video_id` BIGINT(20) NOT NULL ,
    PRIMARY KEY (`id`),
    INDEX(user_id) COMMENT '用于实现喜欢列表,和userDto喜欢的数量统计',
		INDEX (video_id) COMMENT '用于实现videoDto的favorite_count'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '点赞关系表';


#关注列表从userId查
#粉丝列表从toUserId查
#好友列表userId查userId的集合1的toUserId toUserId的集合二的UserId 两者取交集得到好友列表

DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
		`id` BIGINT(20) NOT NULL AUTO_INCREMENT,   
    `user_id` BIGINT(20) NOT NULL,   
    `to_user_id` BIGINT(20) NOT NULL ,
    PRIMARY KEY (`id`),
   INDEX(user_id) COMMENT '查关注列表',
	 INDEX(to_user_id) COMMENT '查粉丝列表'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '关注表';




#消息需要两列联合索引查询来展示消息列表
#user发给friend的列表
#friend发给user的列表
#列表1和列表2再通过时间顺序sort排序实现
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,   
    `to_user_id` BIGINT(20) NOT NULL COMMENT '发送者',
		`from_user_id` BIGINT(20) NOT NULL  COMMENT '接受者',
		`content` TEXT not NULL COMMENT '消息内容',
		`create_time` DATETIME not NULL COMMENT '发送时间',
    PRIMARY KEY (`id`),
	 INDEX multiIdx1(from_user_id,to_user_id) COMMENT '用于查询记录两个索引设置防止联合索引索引失效',
	 INDEX(create_time)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

