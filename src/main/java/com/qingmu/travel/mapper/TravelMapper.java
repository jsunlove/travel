package com.qingmu.travel.mapper;

import com.qingmu.travel.pojo.Travel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 */
//@Mapper
public interface TravelMapper {

    @Insert("INSERT INTO `test`.`business` (`id`, `space`, `item`, `start_date`, `stop_date`, `travel_days`, " +
            "`travel_person`, `finish_work`, `problem_sum`) " +
            "VALUES (null, #{space}, #{item}, #{start_date}, #{stop_date}, #{travel_days}, #{travel_person}, " +
            "#{finish_work}, #{problem_sum})")
    int addTravel(Travel travel);

    @Delete("DELETE FROM business WHERE id = #{id}")
    int deleteTravel(Integer id);

    @Update("UPDATE `test`.`business` SET `space`=#{space}, `item`=#{item}, `start_date`=#{start_date}," +
            "`stop_date`=#{stop_date}, `travel_days`=#{travel_days}, `travel_person`=#{travel_person}," +
            "`finish_work`=#{finish_work}, `problem_sum`=#{problem_sum} WHERE (`id`=#{id});" )
    int updateTravel(Travel travel);

    @Select("SELECT business.* FROM business WHERE id=#{id}")
    Travel getTravelById(Integer id);

    @Select("SELECT business.* FROM business")
    List<Travel> getAllTravel();


    @Select("<script>" +
            "select business.* from business where 1=1\n" +
            "            <if test=\"item != null and item != ''\">\n" +
            "                and item = #{item}\n" +
            "            </if>\n" +
            "            <if test=\"start_date != null and start_date != ''\">\n" +
            "                and start_date = #{start_date}\n" +
            "            </if>\n" +
            "            <if test=\"stop_date != null and stop_date != ''\">\n" +
            "                and stop_date = #{stop_date}\n" +
            "            </if>\n" +
            "    </script>")
    List<Travel> getTravelByRequire(String item, String start_date, String stop_date);

}
