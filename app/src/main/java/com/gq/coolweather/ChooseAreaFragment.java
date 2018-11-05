package com.gq.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gq.coolweather.db.City;
import com.gq.coolweather.db.County;
import com.gq.coolweather.db.Province;
import com.gq.coolweather.util.Contants;
import com.gq.coolweather.util.HttpUtil;
import com.gq.coolweather.util.JsonUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @创建者：刚强
 * @创建时间：2018/10/17 15:52
 * @描述：显示省市县数据
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY     = 1;
    public static final int LEVEL_COUNTY   = 2;

    private TextView titleText;
    private Button   backButton;
    private ListView listView;

    /**
     * 数据适配器
     */
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList();//数据

    /**
     * 省列表
     */
    private List<Province> mProvinceList;
    /**
     * 市列表
     */
    private List<City>     mCityList;
    /**
     * 县列表
     */
    private List<County>   mCountyList;
    /**
     * 进度对话框
     */
    private ProgressDialog mProgressDialog;
    /**当前等级*/
    private int currentLevel;
    /**选中的省份*/
    private Province selectedProvince;
    /**选中的城市*/
    private City selectedCity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //先查询所有省份
        queryProvinces();
        //单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    //选中省份查询所有城市
                    selectedProvince=mProvinceList.get(position);
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    //选中城市查询所有县
                    selectedCity=mCityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){
                    //查询选中县的天气
                    String weatherId=mCountyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        Intent intent=new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra(Contants.WEATHER_ID,weatherId);
                        startActivity(intent);
//                        getActivity().finish();
                    }else if (getActivity()instanceof WeatherActivity){
                        WeatherActivity activity=(WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }



                }
            }
        });
        //返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });

    }


    /**
     * 根据传入的类型查询省市县数据
     * @param address
     * @param type
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求服务器无响应返回主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if("province".equals(type)){
                    result=JsonUtil.handleProvinceReponse(responseText);
                }else if("city".equals(type)){
                    result=JsonUtil.handleCityReponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result=JsonUtil.handleCountyReponse(responseText,selectedCity.getId());
                }
                //查询到数据在主线程中显示
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

        });
    }

    /**
     * 关闭进度提示框
     */
    private void closeProgressDialog() {
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    /**
     * 显示进度提示框
     */
    private void showProgressDialog() {
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(getContext());
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 查询全国所有的省，优先从数据库中选取，如果没有到服务器中查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        mProvinceList= LitePal.findAll(Province.class);
        if(mProvinceList.size()>0){
            dataList.clear();
            for(Province province:mProvinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }

    }


    /**
     * 获取所有县信息
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        mCountyList=LitePal.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(mCountyList.size()>0){
            dataList.clear();
            for(County county:mCountyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    /**
     * 查询选中内所有城市，优先从数据库中选取，如果没有到服务器中查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        mCityList=LitePal.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(mCityList.size()>0){
            dataList.clear();
            for(City city:mCityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }


}
