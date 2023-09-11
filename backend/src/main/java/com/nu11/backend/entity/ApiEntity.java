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
@TableName("t_api")
public class ApiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键 接口唯一标识
	 */
	@TableId
	private Long id;
	/**
	 * 接口名字
	 */
	private String name;
	/**
	 * 接口调用地址
	 */
	private String url;
	/**
	 * 接口调用方法类型
	 */
	private String method;
	/**
	 * 接口描述
	 */
	private String description;
	/**
	 * 接口被调用次数
	 */
	private Long count;

	private Integer status;
	/**
	 * 接口创建时间
	 */
	private Date createTime;
	/**
	 * 接口修改时间
	 */
	private Date updateTime;
	/**
	 * 逻辑删除，0表示为删除，1表示已删除
	 */
	@TableLogic
	private Integer isDelete;

}
