package com.zht.citypicker;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zht.citypicker.dialog.BaseBottomDialog;
import com.zht.citypicker.view.OnWheelChangedListener;
import com.zht.citypicker.view.WheelView;
import com.zht.citypicker.view.adapter.ArrayWheelAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zht on 17/11/29.
 * <p>
 * BaseBottomDialog的实现类
 * 使用方式：
 * <p>
 * CityPicker picker = new CityPicker();
 * picker.setContext(MainActivity.this);
 * picker.setParsedXmlData(parsedXmlData);
 * picker.show(getSupportFragmentManager());
 * picker.setOnClickListener(new CityPicker.onDateSlectedListener() {
 *
 *      @Override
 *      public void onDateSlected(String text) {
 *          textView.setText(text);
 *      }
 * });
 *
 */

public class CityPicker extends BaseBottomDialog implements View.OnClickListener, OnWheelChangedListener {

    private Context context;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mBtnConfirm;
    private TextView mBtnCancel;

    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    protected String mCurrentProviceName = "";
    protected String mCurrentCityName = "";
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";

    private ParsedXmlData parsedXmlData;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setParsedXmlData(ParsedXmlData parsedXmlData) {
        this.parsedXmlData = parsedXmlData;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_bottom_select;
    }

    @Override
    public void addItemView(View v) {
        mViewProvince = (WheelView) v.findViewById(R.id.id_province);
        mViewCity = (WheelView) v.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) v.findViewById(R.id.id_district);
        mBtnConfirm = (TextView) v.findViewById(R.id.dialog_bottom_confirm);
        mBtnCancel = (TextView) v.findViewById(R.id.dialog_bottom_cancel);
    }

    @Override
    public void bindView(View v) {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String selecetText;
        if (id == R.id.dialog_bottom_confirm) {
            selecetText = mCurrentProviceName + "," + mCurrentCityName + ","
                    + mCurrentDistrictName + "," + mCurrentZipCode;
            if (listener != null) {
                listener.onDateSlected(selecetText);
            }
        }
        this.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpData();
    }

    private void setUpData() {

        //数据初始化
        if (parsedXmlData != null) {
            mProvinceDatas = parsedXmlData.getProvinceDatas();
            mCitisDatasMap = parsedXmlData.getCitisDatasMap();
            mDistrictDatasMap = parsedXmlData.getDistrictDatasMap();
            mZipcodeDatasMap = parsedXmlData.getZipcodeDatasMap();
            mCurrentProviceName = parsedXmlData.getCurrentProviceName();
            mCurrentCityName = parsedXmlData.getCurrentCityName();
            mCurrentDistrictName = parsedXmlData.getCurrentDistrictName();
            mCurrentZipCode = parsedXmlData.getCurrentZipCode();

            mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
            // 设置可见条目数量
            mViewProvince.setVisibleItems(7);
            mViewCity.setVisibleItems(7);
            mViewDistrict.setVisibleItems(7);
            updateCities();
            updateAreas();
        }

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }


    /**
     * 数据回调
     *
     * @param listener
     */
    public void setOnClickListener(onDateSlectedListener listener) {
        this.listener = listener;
    }

    private onDateSlectedListener listener;


    /**
     * 选择对话框回调接口，调用者实现
     */
    public interface onDateSlectedListener {
        void onDateSlected(String text);
    }

}