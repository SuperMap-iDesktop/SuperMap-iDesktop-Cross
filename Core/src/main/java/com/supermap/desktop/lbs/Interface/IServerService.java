package com.supermap.desktop.lbs.Interface;

import com.supermap.desktop.lbs.params.JobResultResponse;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by xie on 2017/1/6.
 */
public interface IServerService {

    /**
     * 登录iserver服务
     *
     * @param userName
     * @param passWord
     * @return
     */
    CloseableHttpClient login(String userName, String passWord);

	/**
	 * 根据类型查询结果
	 *
	 * @param type
	 * @param jsonBody
	 * @return
	 */
	JobResultResponse queryResult(String type, String jsonBody);

    /**
     * 查询JSON结果
     *
     * @param url
     * @return
     */
    String query(String url);

}

