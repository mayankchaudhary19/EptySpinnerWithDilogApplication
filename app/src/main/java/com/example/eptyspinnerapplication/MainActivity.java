package com.example.eptyspinnerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int mSelectedIndex = 0;
    private int actualPos = 0;
    private EditText quantityNo;
    private String specifiedQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner spinner = findViewById(R.id.spinner2);
//        String[] years = {"1","2","3","More","hide"};
        final List<String> list = new ArrayList<String>();
        list.add("1");   //  Initial dummy entry
        list.add("2");
        list.add("3");
        list.add("More");
        list.add("");         //4th index


//        int hidingItemIndex = 0;
        final ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                if (position == 3) {
                    ((TextView) v).setText("Qty: ");
                }
                return v;
            }

            public View getDropDownView(final int position, View convertView, ViewGroup parent) {

                View v = super.getDropDownView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                if (position == mSelectedIndex && position < 3) {
                    // Set spinner selected popup item's text color
                    ((TextView) v).setTextColor(Color.parseColor("#FF8A65"));
                } else if (position == 4 || position == 5) {
                    ((TextView) v).setHeight(0);
                }
                return v;
            }


        };


//        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(langAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, final long id) {
                mSelectedIndex = position;

                if (position == 0) {
                    if (!list.get(4).equals("1")) {
                        list.remove(4);
                        list.add(4, "Qty: 1");
                    }
                } else if (position == 1) {
                    if (!list.get(4).equals(" 2")) {
                        list.remove(4);
                        list.add(4, "Qty: 2");
                    }
                } else if (position == 2) {
                    if (!list.get(4).equals(" 3")) {
                        list.remove(4);
                        list.add(4, "Qty: 3");
                    }
                } else if (position == 4) {
                    if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 1")) {
                        spinner.setSelection(0);
                    } else if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 2")) {
                        spinner.setSelection(1);
                    } else if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 3")) {
                        spinner.setSelection(2);
                    }
                }

//                int index = parent.getSelectedItemPosition();

                if (mSelectedIndex == 3) {

                    final Dialog quantityDilog = new Dialog(view.getContext());
                    quantityDilog.setContentView(R.layout.quantity_dialog);
                    quantityDilog.setCancelable(true);
                    quantityDilog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDilog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    quantityNo = quantityDilog.findViewById(R.id.editTextQuantity);
                    //todo: quantityNo Code
                    TextView cancelBtn = quantityDilog.findViewById(R.id.cancelQuantityDialogButton);
                    TextView applyBtn = quantityDilog.findViewById(R.id.applyQuantityDialogButton);

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (list.size()<=5){
                                spinner.setSelection(4);
                            }else spinner.setSelection(5);
                                quantityDilog.dismiss();
                        }
                    });

                    quantityDilog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if (list.size()<=5){
                                spinner.setSelection(4);
                            }else spinner.setSelection(5);
                        }
                    });

                    applyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantityNo.setError(null);
                            specifiedQuantity = quantityNo.getText().toString();
                            if (quantityNo.getText().toString().isEmpty()) {
                                quantityNo.setError("Please Specify Quantity");
                                return;
                            } else if (quantityNo.getText().toString().equals("0")) {
                                quantityNo.setError("Please Remove Item from Cart");
                                return;
                            } else {
                                if (list.size() < 6) {
                                    list.add("");
                                } else {
                                    int index = list.size() - 1;
                                    list.remove(index);
                                    switch (specifiedQuantity) {
                                        case "1":
                                            spinner.setSelection(0);
                                            break;
                                        case "2":
                                            spinner.setSelection(1);
                                            break;
                                        case "3":
                                            spinner.setSelection(2);
                                            break;
                                        default:
                                            list.add(quantityNo.getText().toString());
                                            spinner.setSelection(5);
                                            //                                        if(position==4){
                                            //                                            ((TextView) v).setText("Qty: "+list.get(4));
                                            //                                        }
                                            ((TextView) spinner.getSelectedView()).setText("Qty: " + specifiedQuantity);
                                            mSelectedIndex = 3;
                                            break;
                                    }
                                    quantityDilog.dismiss();
                                }
                            }
                        }
                    });

                    quantityDilog.show();

                } else if (position < 4) {
                    actualPos = position + 1;
                    ((TextView) spinner.getSelectedView()).setText("Qty: " + actualPos);

                }
                if (position == 5) {
                    ((TextView) spinner.getSelectedView()).setText("Qty: " + specifiedQuantity);

                }


                //                ((TextView) parent.getChildAt(0)).setTextColor(0x00000000);
//                ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorAccent)); //<----
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}
//       if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 1")) {
//                                spinner.setSelection(0);
//                            } else if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 2")) {
//                                spinner.setSelection(1);
//                            } else if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 3")) {
//                                spinner.setSelection(2);
//                            } else {
//                                mSelectedIndex = 3;
//                            }

//                            if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 1")) {
//                                spinner.setSelection(0);
//                            } else if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 2")) {
//                                spinner.setSelection(1);
//                            } else if (((TextView) spinner.getSelectedView()).getText().toString().equals("Qty: 3")) {
//                                spinner.setSelection(2);
//                            } else {
//                                mSelectedIndex = 3;
//                            }