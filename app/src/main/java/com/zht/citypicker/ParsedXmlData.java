package com.zht.citypicker;

import android.content.Context;
import android.content.res.AssetManager;

import com.zht.citypicker.model.CityModel;
import com.zht.citypicker.model.DistrictModel;
import com.zht.citypicker.model.ProvinceModel;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author： Tom on 2017/12/6 21:01
 * @email： 820159571@qq.com
 *  
 */
public class ParsedXmlData {

    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    private String mCurrentZipCode = "";

    /**
     * 解析省市区的XML数据
     */
    public void initProvinceDatas(Context context) {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {

                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


    public String[] getProvinceDatas() {
        return mProvinceDatas;
    }

    public void setProvinceDatas(String[] mProvinceDatas) {
        this.mProvinceDatas = mProvinceDatas;
    }

    public Map<String, String[]> getCitisDatasMap() {
        return mCitisDatasMap;
    }

    public void setCitisDatasMap(Map<String, String[]> mCitisDatasMap) {
        this.mCitisDatasMap = mCitisDatasMap;
    }

    public Map<String, String[]> getDistrictDatasMap() {
        return mDistrictDatasMap;
    }

    public void setDistrictDatasMap(Map<String, String[]> mDistrictDatasMap) {
        this.mDistrictDatasMap = mDistrictDatasMap;
    }

    public Map<String, String> getZipcodeDatasMap() {
        return mZipcodeDatasMap;
    }

    public void setZipcodeDatasMap(Map<String, String> mZipcodeDatasMap) {
        this.mZipcodeDatasMap = mZipcodeDatasMap;
    }

    public String getCurrentProviceName() {
        return mCurrentProviceName;
    }

    public void setCurrentProviceName(String mCurrentProviceName) {
        this.mCurrentProviceName = mCurrentProviceName;
    }

    public String getCurrentCityName() {
        return mCurrentCityName;
    }

    public void setCurrentCityName(String mCurrentCityName) {
        this.mCurrentCityName = mCurrentCityName;
    }

    public String getCurrentDistrictName() {
        return mCurrentDistrictName;
    }

    public void setCurrentDistrictName(String mCurrentDistrictName) {
        this.mCurrentDistrictName = mCurrentDistrictName;
    }

    public String getCurrentZipCode() {
        return mCurrentZipCode;
    }

    public void setCurrentZipCode(String mCurrentZipCode) {
        this.mCurrentZipCode = mCurrentZipCode;
    }
}
