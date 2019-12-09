package com.qingmu.travel.service;

import com.qingmu.travel.pojo.Travel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Administrator
 */
public interface TravelService {
    int addTravel(Travel travel);

    int deleteTravel(Integer id);

    int updateTravel(Travel travel);

    Travel getTravelById(Integer id);

    List<Travel> getAllTravel();

    List<Travel> getTravelByRequire(String item, String start_date, String stop_date);

    boolean batchImport(String fileName, MultipartFile file) throws Exception;
}
