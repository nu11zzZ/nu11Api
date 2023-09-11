package com.nu11.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author nu11zzZ
 * @email nu11zzZ@nu11.com
 * @date 2023-08-23 00:09:55
 */
@Data
@TableName("t_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键 用户唯一标识
	 */
	@TableId
	private Long id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 通过第三方登录获取的id
	 */
	private Long oauthId;
	/**
	 * 用户手机号
	 */
	private String phone;
	/**
	 * 用户类型 0为普通用户 1为管理员用户
	 */
	private Integer type;
	/**
	 * 
	 */
	private String accessKey;
	/**
	 * 
	 */
	private String secretKey;
	/**
	 * 用户调用接口的次数，次数为0则不能调用接口
	 */
	private Long apiCount;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 逻辑删除，0为未删除 1为已删除
	 */
	@TableLogic
	private Integer isDelete;

}
