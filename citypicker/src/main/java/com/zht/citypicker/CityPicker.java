package com.zht.citypicker;

import android.content.Context;

/**
 * @author： Tom on 2017/12/6 21:01
 * @email： 820159571@qq.com
 *  
 */
public class CityPicker {

    private Context context;
    private ParsedXmlData parsedXmlData;


    public CityPicker(Context context) {
        this.context = context;
        parsedXmlData = new ParsedXmlData();
        //解析Xml中的数据到parsedXmlData类中
        parsedXmlData.initProvinceDatas(context);
    }

    public ParsedXmlData getParsedXmlData() {
        return parsedXmlData;
    }

    public void setParsedXmlData(ParsedXmlData parsedXmlData) {
        this.parsedXmlData = parsedXmlData;
    }
}
