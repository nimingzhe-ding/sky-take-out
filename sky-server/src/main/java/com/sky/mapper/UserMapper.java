
package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据动态条件查询
     * @param map
     * @return
     */
     Integer countByMap(Map map);

    /**
       * 根据openid查询用户
       * @param openId
       * @return
       */
      @Select("select *from  user where openid = #{openid}")

        User getByOpenId(String openId);

    /**
     * 插入用户数据
     * @param user
     */
    void insert(User user);
}

