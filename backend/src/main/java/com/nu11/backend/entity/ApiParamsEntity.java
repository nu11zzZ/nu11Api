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
@TableName("t_api_params")
public class ApiParamsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 接口id
	 */
	private Long apiId;
	/**
	 * 接口参数的类型如Header Query Body
	 */
	private String type;
	/**
	 * 参数名字
	 */
	private String name;
	/**
	 * 参数的示例值
	 */
	private String value;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 逻辑删除字段 0为未删除 1为已删除
	 */
	@TableLogic
	private Integer isDelete;

}
