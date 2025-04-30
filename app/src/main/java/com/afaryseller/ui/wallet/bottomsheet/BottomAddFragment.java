package com.afaryseller.ui.wallet.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afaryseller.R;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.splash.AskListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class BottomAddFragment extends BottomSheetDialogFragment implements AskListener {

    Context context;
    private RelativeLayout done_text;
    private AfarySeller apiInterface;
    private EditText money_et;

    AskListener listener;

    public BottomAddFragment(Context context) {
        this.context = context;
    }


    public BottomAddFragment callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragmen_add_money, (ViewGroup) null);

        apiInterface = ApiClient.getClient(context).create(AfarySeller.class);

        done_text = contentView.findViewById(R.id.done_text);
        money_et = contentView.findViewById(R.id.money_et);

        done_text.setOnClickListener(v -> {
           // AddWalletAPI(money_et.getText().toString());
            if(money_et.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(context, "Please enter amount", Toast.LENGTH_SHORT).show();
            else {
                new PaymentBottomSheet(money_et.getText().toString()).callBack(this::ask).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");

            }
        });


        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

/*
    private void AddWalletAPI(String addmoney) {

        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");

        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +PreferenceConnector.readString(getActivity(), PreferenceConnector.access_token,""));
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferenceConnector.readString(getContext(), PreferenceConnector.User_id, ""));
        map.put("money", addmoney);

        Log.e("MapMap", "LOGIN REQUEST" + map);

        Call<AddWalletModal> SignupCall = apiInterface.add_money(headerMap,map);

        SignupCall.enqueue(new Callback<AddWalletModal>() {
            @Override
            public void onResponse(Call<AddWalletModal> call, Response<AddWalletModal> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    AddWalletModal data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "LOGIN RESPONSE" + dataResponse);

                    if (data.status.equals("1")) {
                        dismiss();
                        Toast.makeText(getContext(), "Your Money Is Add SuccessFully ", Toast.LENGTH_SHORT).show();

                    } else if (data.status.equals("0")) {
                        dismiss();
                        Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddWalletModal> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
*/

    @Override
    public void ask(String value,String status) {
        Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
        listener.ask("","payment");
        dismiss();
    }
}
