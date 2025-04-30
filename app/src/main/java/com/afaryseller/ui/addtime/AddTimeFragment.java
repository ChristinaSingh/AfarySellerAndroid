package com.afaryseller.ui.addtime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.FragmentAddTimeBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addshop.AddShopFragment;
import com.afaryseller.ui.addshop.CountryAdapter;
import com.afaryseller.ui.addshop.StateAdapter;
import com.afaryseller.ui.addshop.adapter.CityAdapter;
import com.afaryseller.ui.addshop.model.CountryModel;
import com.afaryseller.ui.addshop.model.StateModel;
import com.afaryseller.ui.addtime.adapter.CloseAdapter;
import com.afaryseller.ui.addtime.adapter.HolidaysAdapter;
import com.afaryseller.ui.addtime.adapter.TimeAdapter;
import com.afaryseller.ui.addtime.adapter.WeeklyAdapter;
import com.afaryseller.ui.addtime.listener.AddDateLister;
import com.afaryseller.ui.addtime.model.HolidaysModel;
import com.afaryseller.ui.editTime.EditTimeFragment;
import com.afaryseller.ui.signup.CityModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddTimeFragment extends BaseFragment<FragmentAddTimeBinding, AddTimeViewModel> implements AddDateLister {
    FragmentAddTimeBinding binding;
    AddTimeViewModel addTimeViewModel;
    String shopId = "",sub_categary_id="",name="";
    ArrayList<TimeModel.Result> openTimeList;
    ArrayList<TimeModel.Result> closeTimeList;
    ArrayList<TimeModel.Result> weeklyList;

    ArrayList<HolidaysModel.Result> holiArrayList;

    TimeAdapter timeAdapter;
    CloseAdapter closeAdapter;
    String selectOpenTime="0",selectCloseTime="0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_time, container, false);
        addTimeViewModel = new AddTimeViewModel();
        binding.setAddTimeViewModel(addTimeViewModel);
        binding.getLifecycleOwner();
        addTimeViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        if(this.getArguments()!=null) {
            shopId = getArguments().getString("shop_id");
            sub_categary_id = getArguments().getString("id");
            name = getArguments().getString("name");
        }
      //  shopId = "24";
        openTimeList = new ArrayList<>();
        closeTimeList = new ArrayList<>();
        weeklyList = new ArrayList<>();
        holiArrayList = new ArrayList<>();

        timeAdapter = new TimeAdapter(getActivity(), openTimeList, AddTimeFragment.this);
        binding.recyclerViewOpenTime.setAdapter(timeAdapter);

        closeAdapter = new CloseAdapter(getActivity(), closeTimeList, AddTimeFragment.this);
        binding.recyclerViewCloseTime.setAdapter(closeAdapter);


        binding.layoutHolidays.setOnClickListener(v -> DatePicker(getActivity()));

     //   putDataList();

        observeLoader();
        observeResponse();

        callApi();


        binding.submit.setOnClickListener(v -> {

            for (int i=0;i<openTimeList.size();i++){
                if(openTimeList.get(i).getStatus().equalsIgnoreCase("0")){
                    selectOpenTime = openTimeList.get(i).getStatus();
                    break;
                }
            }

            for (int j=0;j < closeTimeList.size();j++){
                Log.e("close time list==="+ j,closeTimeList.get(j).getStatus());
                if(closeTimeList.get(j).getStatus().equalsIgnoreCase("0")){
                    selectCloseTime = closeTimeList.get(j).getStatus();
                    break;
                }
            }


            if(selectOpenTime.equalsIgnoreCase("0")) Toast.makeText(getActivity(), getString(R.string.please_add_open_time), Toast.LENGTH_SHORT).show();
            else if(selectCloseTime.equalsIgnoreCase("0")) Toast.makeText(getActivity(), getString(R.string.please_add_close_tiem), Toast.LENGTH_SHORT).show();
                else{
                Bundle bundle = new Bundle();
                bundle.putString("id",sub_categary_id);
                bundle.putString("name",name);
                Navigation.findNavController(v).navigate(R.id.action_addTime_fragment_to_shoplist,bundle);
          }
        });

        binding.tvOpenTime.setOnClickListener(view ->openTimePicker(0,"Select Open Time:") );

        binding.tvCloseTime.setOnClickListener(view -> openTimePicker(0,"Select Close Time:"));


    }


    public void observeResponse(){
        addTimeViewModel.isResponse.observe(getActivity(),dynamicResponseModel -> {
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
                                binding.recyclerViewDays.setAdapter(new WeeklyAdapter(getActivity(), weeklyList,AddTimeFragment.this));
                               // openTimeList.clear();
                             //   closeTimeList.clear();


                                for (int i = 0; i < weeklyList.size(); i++) {
                                    // Assuming getDate() or getId() gives a unique identifier for each day.
                                    String weekName = weeklyList.get(i).getWeekName(); // Or use getId(), depending on your data structure.

                                    boolean isDateInOpenTimeList = false;
                                    boolean isDateInCloseTimeList = false;

                                    // Check if the date already exists in openTimeList or closeTimeList
                                    for (TimeModel.Result item : openTimeList) {
                                        if (item.getWeekName().equals(weekName)) {
                                            isDateInOpenTimeList = true;
                                            break;
                                        }
                                    }

                                    for (TimeModel.Result item : closeTimeList) {
                                        if (item.getWeekName().equals(weekName)) {
                                            isDateInCloseTimeList = true;
                                            break;
                                        }
                                    }

                                    if (weeklyList.get(i).getStatus().equals("0")) {
                                        if (!isDateInOpenTimeList) {
                                            openTimeList.add(weeklyList.get(i));  // Add to openTimeList if not present
                                        }
                                        if (!isDateInCloseTimeList) {
                                            closeTimeList.add(weeklyList.get(i));  // Add to closeTimeList if not present
                                        }
                                    }
                                    else if (weeklyList.get(i).getStatus().equals("1")){
                                         for(int ii = 0;ii< openTimeList.size() ;ii++) {
                                              if(openTimeList.get(ii).getWeekName().equals(weeklyList.get(i).getWeekName()))openTimeList.remove(ii);
                                         }

                                        for(int jj = 0;jj< closeTimeList.size() ;jj++) {
                                            if(closeTimeList.get(jj).getWeekName().equals(weeklyList.get(i).getWeekName()))closeTimeList.remove(jj);
                                        }

                                    }


                                }



/*
                                for (int i =0;i<weeklyList.size();i++){
                                    if(weeklyList.get(i).getStatus().equals("0")){
                                     //  openTimeList.add(weeklyList.get(i));
                                    //   closeTimeList.add(weeklyList.get(i));

                                        if (!openTimeList.contains(weeklyList.get(i))) {
                                            openTimeList.add(weeklyList.get(i));
                                        }
                                        if (!closeTimeList.contains(weeklyList.get(i))) {
                                            closeTimeList.add(weeklyList.get(i));
                                        }



                                    }
                                }
*/





                                timeAdapter.notifyDataSetChanged();
                                closeAdapter.notifyDataSetChanged();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                binding.rvHolidays.setAdapter(new HolidaysAdapter(getActivity(),holiArrayList,AddTimeFragment.this));
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                holiArrayList.clear();
                                binding.rvHolidays.setAdapter(new HolidaysAdapter(getActivity(),holiArrayList,AddTimeFragment.this));

                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }






            }
        } );
    }

    private void callGetHolidayApi() {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("shop_id",shopId);
        addTimeViewModel.getAllHolidays(getActivity(),headerMap,map);
    }


    private void callAddHolidayApi(String date) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("shop_id",shopId);
        map.put("holidays_date",date);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        addTimeViewModel.addAllHolidays(getActivity(),headerMap,map);
    }


    private void callApi() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("shop_id",shopId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        addTimeViewModel.getAllDailyCloseDay(getActivity(),headerMap,map);
    }

    private void observeLoader() {
        addTimeViewModel.isLoading.observe(getActivity(),aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    private void callUpdateApi(String id,String status) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("status",status);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        addTimeViewModel.addAllDailyCloseDay(getActivity(),headerMap,map);
    }


    private void callAddOpenTimeApi(String id,String openTime,String dayId) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("open_time",openTime);
        map.put("day_id",dayId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        addTimeViewModel.addAOpenTime(getActivity(),headerMap,map);
    }


    private void callAddCloseTimeApi(String id,String closeTime,String dayId) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("close_time",closeTime);
        map.put("day_id",dayId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        addTimeViewModel.addACloseTime(getActivity(),headerMap,map);
    }


    private void deleteApi(String id) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("id", id);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        addTimeViewModel.deleteHolidays(getActivity(),headerMap,map);
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
        else  if(tag.equals("daily close day")) callUpdateApi(weeklyList.get(position).getId(),date);
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
                  selectOpenTime = time;
                 /* for (int i= 0;i<openTimeList.size();i++) {
                      openTimeList.get(i).setTime(time);
                      openTimeList.get(i).setStatus("1");
                  }
                  timeAdapter.notifyDataSetChanged();
                  //timeAdapter.notifyItemChanged(position);
                  callAddOpenTimeApi( shopId,time);*/



                  openTimeList.get(position).setTime(time);
                  openTimeList.get(position).setStatus("1");
                  timeAdapter.notifyItemChanged(position);
                  callAddOpenTimeApi(shopId,time, openTimeList.get(position).getId());

              }
              else {
                  selectCloseTime = time;
                 /* for (int i= 0;i<closeTimeList.size();i++) {
                      closeTimeList.get(i).setTime(time);
                      closeTimeList.get(i).setStatus("1");
                  }
                  closeAdapter.notifyDataSetChanged();
                  // closeAdapter.notifyItemChanged(position);
                 // callAddCloseTimeApi( closeTimeList.get(position).getId(),time);

                  callAddCloseTimeApi( shopId,time);*/

                  closeTimeList.get(position).setTime(time);
                  closeTimeList.get(position).setStatus("1");
                  closeAdapter.notifyItemChanged(position);
                  callAddCloseTimeApi(shopId,time,openTimeList.get(position).getId());

              }

                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
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
