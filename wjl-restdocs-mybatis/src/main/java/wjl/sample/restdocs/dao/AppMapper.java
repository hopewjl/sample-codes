package wjl.sample.restdocs.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import wjl.sample.restdocs.App;
import wjl.sample.restdocs.AppExample;

@Mapper
public interface AppMapper {
    @SelectProvider(type = AppSqlProvider.class, method = "countByExample")
    int countByExample(AppExample example);

    @DeleteProvider(type = AppSqlProvider.class, method = "deleteByExample")
    int deleteByExample(AppExample example);

    @Delete({
            "delete from app",
            "where appid = #{appid,jdbcType=SMALLINT}"
    })
    int deleteByPrimaryKey(Short appid);

    @Insert({
            "insert into app (name, ",
            "creator, describe)",
            "values (#{name,jdbcType=VARCHAR}, ",
            "#{creator,jdbcType=VARCHAR}, #{describe,jdbcType=VARCHAR})"
    })
    int insert(App record);

    @InsertProvider(type = AppSqlProvider.class, method = "insertSelective")
    int insertSelective(App record);

    @SelectProvider(type = AppSqlProvider.class, method = "selectByExample")
    @Results({
            @Result(column = "appid", property = "appid", jdbcType = JdbcType.SMALLINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "creator", property = "creator", jdbcType = JdbcType.VARCHAR),
            @Result(column = "describe", property = "describe", jdbcType = JdbcType.VARCHAR)
    })
    List<App> selectByExample(AppExample example);

    @Select({
            "select",
            "appid, name, creator, describe",
            "from app",
            "where appid = #{appid,jdbcType=SMALLINT}"
    })
    @Results({
            @Result(column = "appid", property = "appid", jdbcType = JdbcType.SMALLINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "creator", property = "creator", jdbcType = JdbcType.VARCHAR),
            @Result(column = "describe", property = "describe", jdbcType = JdbcType.VARCHAR)
    })
    App selectByPrimaryKey(Short appid);

    @UpdateProvider(type = AppSqlProvider.class, method = "updateByExampleSelective")
    int updateByExampleSelective(@Param("record") App record, @Param("example") AppExample example);

    @UpdateProvider(type = AppSqlProvider.class, method = "updateByExample")
    int updateByExample(@Param("record") App record, @Param("example") AppExample example);

    @UpdateProvider(type = AppSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(App record);

    @Update({
            "update app",
            "set name = #{name,jdbcType=VARCHAR},",
            "creator = #{creator,jdbcType=VARCHAR},",
            "describe = #{describe,jdbcType=VARCHAR}",
            "where appid = #{appid,jdbcType=SMALLINT}"
    })
    int updateByPrimaryKey(App record);
}