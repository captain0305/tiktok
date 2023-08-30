package org.example.interact.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id Mybatis plus自动生成
     */
    @TableId
    private Long userId;

    /**
     * 随用户注册上传
     */
    private String name;

    /**
     * 密码通过md5	加密后存储
     */
    private String password;

    /**
     * 头像可以更换——默认为初始照片上传到服务器中
     */
    private String avatar;

    /**
     * 背景图片可以更换——默认为初始照片上传到服务器中
     */
    private String backgroudImage;

    /**
     * 个性签名--默认为空字符串可以更改
     */
    private String sinature;

    /**
     * 关注总数
     */
    private Integer followCount;

    /**
     * 粉丝总数
     */
    private Integer followerCount;

    /**
     * 获赞总数
     */
    private Integer totalFavorited;

    /**
     * 作品总数
     */
    private Integer workCount;

    /**
     * 获赞总数
     */
    private Integer favoriteCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getBackgroudImage() == null ? other.getBackgroudImage() == null : this.getBackgroudImage().equals(other.getBackgroudImage()))
            && (this.getSinature() == null ? other.getSinature() == null : this.getSinature().equals(other.getSinature()))
            && (this.getFollowCount() == null ? other.getFollowCount() == null : this.getFollowCount().equals(other.getFollowCount()))
            && (this.getFollowerCount() == null ? other.getFollowerCount() == null : this.getFollowerCount().equals(other.getFollowerCount()))
            && (this.getTotalFavorited() == null ? other.getTotalFavorited() == null : this.getTotalFavorited().equals(other.getTotalFavorited()))
            && (this.getWorkCount() == null ? other.getWorkCount() == null : this.getWorkCount().equals(other.getWorkCount()))
            && (this.getFavoriteCount() == null ? other.getFavoriteCount() == null : this.getFavoriteCount().equals(other.getFavoriteCount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getBackgroudImage() == null) ? 0 : getBackgroudImage().hashCode());
        result = prime * result + ((getSinature() == null) ? 0 : getSinature().hashCode());
        result = prime * result + ((getFollowCount() == null) ? 0 : getFollowCount().hashCode());
        result = prime * result + ((getFollowerCount() == null) ? 0 : getFollowerCount().hashCode());
        result = prime * result + ((getTotalFavorited() == null) ? 0 : getTotalFavorited().hashCode());
        result = prime * result + ((getWorkCount() == null) ? 0 : getWorkCount().hashCode());
        result = prime * result + ((getFavoriteCount() == null) ? 0 : getFavoriteCount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", name=").append(name);
        sb.append(", password=").append(password);
        sb.append(", avatar=").append(avatar);
        sb.append(", backgroudImage=").append(backgroudImage);
        sb.append(", sinature=").append(sinature);
        sb.append(", followCount=").append(followCount);
        sb.append(", followerCount=").append(followerCount);
        sb.append(", totalFavorited=").append(totalFavorited);
        sb.append(", workCount=").append(workCount);
        sb.append(", favoriteCount=").append(favoriteCount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}