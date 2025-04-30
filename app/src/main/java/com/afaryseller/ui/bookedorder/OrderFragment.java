package com.afaryseller.ui.bookedorder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.OrderFragmentBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.notifications.NotificationAct;
import com.afaryseller.ui.orderdetails.OrderDetailsAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends BaseFragment<OrderFragmentBinding, OrderViewModel> implements OrderListener {
    OrderFragmentBinding binding;
    OrderViewModel orderViewModel;
    String catId="";
    OrderAdapter adapter;
    ArrayList<OrderModel.Result> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_fragment, container, false);
        orderViewModel = new OrderViewModel();
        binding.setOrderViewModel(orderViewModel);
        binding.getLifecycleOwner();
        orderViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {

        if(this.getArguments()!=null) {
            catId  = getArguments().getString("id");

        }
        arrayList = new ArrayList<>();

      adapter =  new OrderAdapter(getActivity(), arrayList,OrderFragment.this);
      binding.rvOrders.setAdapter(adapter);


        observeLoader();
            observeResponse();

            binding.RRback.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void callGetAllOrder() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("cat_id", catId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        map.put("sub_seller_id", DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id());
        map.put("shop_id",SessionManager.readString(requireActivity(), Constant.shopId, ""));

        orderViewModel.getAllOrder(getActivity(),headerMap,map);
    }

    private void callAcceptDeclineOrder(String status,String type,String orderId,String userId,String reason) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id",DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("user_id",userId);
        map.put("order_id",orderId);
        map.put("status", status);
        map.put("type", type);
        map.put("reason", reason);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

      //  map.put("sub_seller_id", DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id());
      //  map.put("shop_id",SessionManager.readString(requireActivity(), Constant.shopId, ""));

        Log.e("orderAcceptDecline====",map.toString());

        orderViewModel.acceptDeclineOrder(getActivity(),headerMap,map);
    }


    public void observeResponse(){
                orderViewModel.isResponse.observe(getActivity(),dynamicResponseModel -> {
                        if(dynamicResponseModel.getJsonObject()!=null){
                                pauseProgressDialog();
                                if(dynamicResponseModel.getApiName()== ApiConstant.GET_ALL_ORDER){
                                        try {
                                                if(dynamicResponseModel.getCode()==200){
                                                        Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                                                        String stringResponse = dynamicResponseModel.getJsonObject().string();
                                                        JSONObject jsonObject = new JSONObject(stringResponse);
                                                        if (jsonObject.getString("status").toString().equals("1")) {
                                                              // binding.tvNotFount.setVisibility(View.GONE);
                                                                OrderModel model = new Gson().fromJson(stringResponse,OrderModel.class);
                                                            binding.tvNotFound.setVisibility(View.GONE);
                                                            arrayList.clear();
                                                                arrayList.addAll( model.getResult());
                                                                adapter.notifyDataSetChanged();
                                                        } else if (jsonObject.getString("status").toString().equals("0")) {
                                                               // binding.tvNotFount.setVisibility(View.VISIBLE);
                                                            binding.tvNotFound.setVisibility(View.VISIBLE);
                                                            arrayList.clear();
                                                            adapter.notifyDataSetChanged();
                                                           // Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        else if (jsonObject.getString("status").toString().equals("5")) {
                                                            SessionManager.logout(getActivity());
                                                        }
                                                }
                                                else {
                                                      //  binding.tvNotFount.setVisibility(View.VISIBLE);
                                                        Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                                }

                                        }catch (Exception e) {
                                                e.printStackTrace();
                                        }
                                }


                                if(dynamicResponseModel.getApiName()== ApiConstant.ACCEPT_DECLINE_ORDER){
                                    try {
                                        if(dynamicResponseModel.getCode()==200){
                                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                                            JSONObject jsonObject = new JSONObject(stringResponse);
                                            if (jsonObject.getString("status").toString().equals("1")) {
                                                // binding.tvNotFount.setVisibility(View.GONE);
                                                Toast.makeText(getActivity(), getString(R.string.order_cancelled), Toast.LENGTH_SHORT).show();
                                                callGetAllOrder();

                                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                            }

                                            else if (jsonObject.getString("status").toString().equals("5")) {
                                                SessionManager.logout(getActivity());
                                            }

                                        }
                                        else {
                                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                        }
                } );
        }

        private void observeLoader() {
                orderViewModel.isLoading.observe(getActivity(),aBoolean -> {
                        if (aBoolean) {
                                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
                        }else{
                                pauseProgressDialog();
                        }
                });
        }


    @Override
    public void onResume() {
        super.onResume();
        callGetAllOrder();

    }

    @Override
    public void onOrder(OrderModel.Result result) {
      //  alertCancelOrder("Cancelled",result.getOrderId(),result.getUserId());
        dialogCancelOrderReason("Cancelled","All",result.getOrderId(),result.getUserId());

    }



    private void dialogCancelOrderReason(String orderStatus,String type,String orderId,String userId) {

        Dialog mDialog = new Dialog(requireActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_cancel_reason);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edReason = mDialog.findViewById(R.id.edReason);
        AppCompatButton btnCancel = mDialog.findViewById(R.id.btnCancel);
        AppCompatButton btnSubmit = mDialog.findViewById(R.id.btnSubmit);

        btnCancel.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnSubmit.setOnClickListener(v -> {
            mDialog.dismiss();
            // callCompleteSelfCollect(edReason.getText().toString());
            //  callAcceptDeclineOrder(orderStatus,type,orderId,edReason.getText().toString());
            callAcceptDeclineOrder(orderStatus,type,orderId,userId,edReason.getText().toString());


        });
        mDialog.show();
    }



    private void alertCancelOrder(String orderStatus,String orderId,String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to refuse this order?")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                     //   callAcceptDeclineOrder(orderStatus,orderId,userId);
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


}