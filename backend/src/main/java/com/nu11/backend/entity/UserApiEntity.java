package com.nu11.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_user_api")
public class UserApiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 关系表id
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 接口id
	 */
	private Long apiId;
	/**
	 * 该用户调用该接口的次数
	 */
	private Long count;
	/**
	 * 创建时间也是用户首次调用时间
	 */
	private Date createTime;
	/**
	 * 修改时间也是用户最后一次调用时间
	 */
	private Date updateTime;
	/**
	 * 逻辑删除字段 0为未删除 1为已删除
	 */
	private Integer isDelete;

}
