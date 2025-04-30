package com.afaryseller.ui.editTime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.FragmentEditTimeBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addtime.TimeModel;
import com.afaryseller.ui.addtime.adapter.HolidaysAdapter;
import com.afaryseller.ui.addtime.adapter.WeeklyAdapter;
import com.afaryseller.ui.addtime.listener.AddDateLister;
import com.afaryseller.ui.addtime.model.HolidaysModel;
import com.afaryseller.ui.editTime.adapter.CloseTimeAdapter;
import com.afaryseller.ui.editTime.adapter.EditTimeAdapter;
import com.afaryseller.ui.editTime.model.CloseTimeModel;
import com.afaryseller.ui.editTime.model.OpenTimeModel;
import com.afaryseller.ui.shopdetails.ShopDetailsFragment;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditTimeFragment extends BaseActivity<FragmentEditTimeBinding, EditTimeViewModel> implements AddDateLister {
    FragmentEditTimeBinding binding;
    EditTimeViewModel editTimeViewModel;
    String shopId = "",sub_categary_id="",name="";
    ArrayList<OpenTimeModel.Result> openTimeList;
    ArrayList<CloseTimeModel.Result> closeTimeList;
    ArrayList<TimeModel.Result> weeklyList;

    ArrayList<HolidaysModel.Result> holiArrayList;

    EditTimeAdapter timeAdapter;
    CloseTimeAdapter closeAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_edit_time);
        editTimeViewModel = new EditTimeViewModel();
        binding.setEditTimeViewModel(editTimeViewModel);
        binding.getLifecycleOwner();
        editTimeViewModel.init(EditTimeFragment.this);
        initViews();
    }



    private void initViews() {
        if(getIntent()!=null) {
            shopId = getIntent().getStringExtra("shop_id");
          ///  sub_categary_id = getArguments().getString("id");
            name = getIntent().getStringExtra("name");
        }
        //  shopId = "24";
        openTimeList = new ArrayList<>();
        closeTimeList = new ArrayList<>();
        weeklyList = new ArrayList<>();
        holiArrayList = new ArrayList<>();

        timeAdapter = new EditTimeAdapter(EditTimeFragment.this, openTimeList, EditTimeFragment.this);
        binding.recyclerViewOpenTime.setAdapter(timeAdapter);

        closeAdapter = new CloseTimeAdapter(EditTimeFragment.this, closeTimeList, EditTimeFragment.this);
        binding.recyclerViewCloseTime.setAdapter(closeAdapter);


        binding.layoutHolidays.setOnClickListener(v -> DatePicker(EditTimeFragment.this));

        //   putDataList();

        observeLoader();
        observeResponse();

        callApi();

        callGetHolidayApi();

        binding.rBack.setOnClickListener(v -> finish());


        binding.submit.setOnClickListener(v -> {
            startActivity(new Intent(EditTimeFragment.this, ShopDetailsFragment.class)
                    .putExtra("shop_id",shopId).putExtra("name",name));
            finish();
        });

    }


    public void observeResponse(){
        editTimeViewModel.isResponse.observe(EditTimeFragment.this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.GET_DAILY_CLOSE_DAY){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get daily close Day===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                TimeModel timeModel = new Gson().fromJson(stringResponse, TimeModel.class);
                                weeklyList.clear();
                                weeklyList.addAll(timeModel.getResult());
                                binding.recyclerViewDays.setAdapter(new WeeklyAdapter(EditTimeFragment.this, weeklyList,EditTimeFragment.this));
                                callGetOpenTimeApi();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }
                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(dynamicResponseModel.getApiName()== ApiConstant.GET_EDIT_OPEN_TIME){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get open time===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                OpenTimeModel openTimeModel = new Gson().fromJson(stringResponse, OpenTimeModel.class);
                                openTimeList.clear();
                                for (int i =0;i<openTimeModel.getResult().size();i++){
                                    if(openTimeModel.getResult().get(i).getStatus().equals("0"))
                                        openTimeList.add(openTimeModel.getResult().get(i));
                                }
                               // openTimeList.addAll(openTimeModel.getResult());
                                timeAdapter.notifyDataSetChanged();
                                callGetCloseTimeApi();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(dynamicResponseModel.getApiName()== ApiConstant.GET_EDIT_CLOSE_TIME){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get close time===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                CloseTimeModel closeTimeModel = new Gson().fromJson(stringResponse, CloseTimeModel.class);
                                closeTimeList.clear();
                               // closeTimeList.addAll(closeTimeModel.getResult());
                                for (int i =0;i<closeTimeModel.getResult().size();i++){
                                    if(closeTimeModel.getResult().get(i).getStatus().equals("0"))
                                        closeTimeList.add(closeTimeModel.getResult().get(i));
                                }
                                closeAdapter.notifyDataSetChanged();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.ADD_DAILY_CLOSE_DAY){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get states===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                callApi();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(dynamicResponseModel.getApiName()== ApiConstant.ADD_OPEN_TIME){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get states===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                //  callApi();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.ADD_CLOSE_TIME){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get states===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                //  callApi();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }


                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.ADD_HOLIDAY){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get states===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                callGetHolidayApi();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.GET_HOLIDAY){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get states===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                HolidaysModel holidaysModel = new Gson().fromJson(stringResponse, HolidaysModel.class);
                                holiArrayList.clear();
                                holiArrayList.addAll(holidaysModel.getResult());
                                binding.rvHolidays.setAdapter(new HolidaysAdapter(EditTimeFragment.this,holiArrayList,EditTimeFragment.this));
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                holiArrayList.clear();
                                binding.rvHolidays.setAdapter(new HolidaysAdapter(EditTimeFragment.this,holiArrayList,EditTimeFragment.this));

                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.DELETE_HOLIDAY){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get states===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                callGetHolidayApi();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditTimeFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditTimeFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(EditTimeFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        } );
    }

    private void callGetHolidayApi() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());
        map.put("shop_id",shopId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        editTimeViewModel.getAllHolidays(EditTimeFragment.this,headerMap,map);
    }


    private void callAddHolidayApi(String date) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());
        map.put("shop_id",shopId);
        map.put("holidays_date",date);
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        editTimeViewModel.addAllHolidays(EditTimeFragment.this,headerMap,map);
    }


    private void callApi() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        map.put("shop_id",shopId);
        editTimeViewModel.getAllDailyCloseDay(EditTimeFragment.this,headerMap,map);
    }

    private void callGetOpenTimeApi() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");
        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        map.put("shop_id",shopId);
        editTimeViewModel.getEditOpenTime(EditTimeFragment.this,headerMap,map);
    }


    private void callGetCloseTimeApi() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());
        map.put("shop_id",shopId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        editTimeViewModel.getEditCloseTime(EditTimeFragment.this,headerMap,map);
    }



    private void observeLoader() {
        editTimeViewModel.isLoading.observe(EditTimeFragment.this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(EditTimeFragment.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    private void callUpdateApi(String id,String status) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("status",status);
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        editTimeViewModel.addAllDailyCloseDay(EditTimeFragment.this,headerMap,map);
    }


    private void callAddOpenTimeApi(String id,String openTime) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("open_time",openTime);
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        editTimeViewModel.addAOpenTime(EditTimeFragment.this,headerMap,map);
    }


    private void callAddCloseTimeApi(String id,String closeTime) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("close_time",closeTime);
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        editTimeViewModel.addACloseTime(EditTimeFragment.this,headerMap,map);
    }


    private void deleteApi(String id) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("seller_register_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(EditTimeFragment.this).getResult().getId());

        editTimeViewModel.deleteHolidays(EditTimeFragment.this,headerMap,map);
    }


    private void putDataList() {
      /*  weeklyList.add(new TimeModel("1", "Sunday", "", false));
        weeklyList.add(new TimeModel("2", "Monday", "", false));
        weeklyList.add(new TimeModel("3", "Tuesday", "", false));
        weeklyList.add(new TimeModel("4", "Wednesday", "", false));
        weeklyList.add(new TimeModel("5", "Thursday", "", false));
        weeklyList.add(new TimeModel("6", "Friday", "", false));
        weeklyList.add(new TimeModel("7", "Saturday", "", false));


        openTimeList.add(new TimeModel("1", "Sunday", "", false));
        openTimeList.add(new TimeModel("2", "Monday", "", false));
        openTimeList.add(new TimeModel("3", "Tuesday", "", false));
        openTimeList.add(new TimeModel("4", "Wednesday", "", false));
        openTimeList.add(new TimeModel("5", "Thursday", "", false));
        openTimeList.add(new TimeModel("6", "Friday", "", false));
        openTimeList.add(new TimeModel("7", "Saturday", "", false));

        closeTimeList.add(new TimeModel("1", "Sunday", "", false));
        closeTimeList.add(new TimeModel("2", "Monday", "", false));
        closeTimeList.add(new TimeModel("3", "Tuesday", "", false));
        closeTimeList.add(new TimeModel("4", "Wednesday", "", false));
        closeTimeList.add(new TimeModel("5", "Thursday", "", false));
        closeTimeList.add(new TimeModel("6", "Friday", "", false));
        closeTimeList.add(new TimeModel("7", "Saturday", "", false));*/





    }

    @Override
    public void onDate(String date, int position, String tag, boolean chk) {
        if(tag.equals("open")) openTimePicker(position,"Select Open Time:");
        else  if(tag.equals("close"))  openTimePicker(position,"Select Close Time:");
        else if(tag.equals("daily close day")) callUpdateApi(weeklyList.get(position).getId(),date);
    }

    @Override
    public void onDate(String date, int position, String tag) {
        deleteApi(date);
    }


    public void openTimePicker(int position,String title) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    Calendar date = Calendar.getInstance();
                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    date.set(Calendar.MINUTE, minute);
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    String time=new SimpleDateFormat("hh:mm aa").format(date.getTime());
                    if(title.equals("Select Open Time:")) {
                        openTimeList.get(position).setOpenTime(time);
                        openTimeList.get(position).setStatus("1");
                        timeAdapter.notifyItemChanged(position);
                        callAddOpenTimeApi( openTimeList.get(position).getId(),time);
                    }
                    else {
                        closeTimeList.get(position).setCloseTime(time);
                        closeTimeList.get(position).setStatus("1");
                        closeAdapter.notifyItemChanged(position);
                        callAddCloseTimeApi( closeTimeList.get(position).getId(),time);

                    }

                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(EditTimeFragment.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle(title);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }


    public  void DatePicker(Context context) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format yyyy-MM-dd"
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                callAddHolidayApi(sdf.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog datePickerDialog= new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        // datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis()+ (1000*60*60*24*10));
        Log.e("-------",myCalendar.getTimeInMillis()+"");

        datePickerDialog.show();
    }


    // user_id=137&shop_id=24&=06-03-2023
}
