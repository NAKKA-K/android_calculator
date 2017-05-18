package com.example.snakka.helloandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AnotherCalcActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{
    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;
    private static final int REQUEST_CODE_ANOTHER_CALC_2 = 2;

    private EditText numInput1;
    private EditText numInput2;
    private Spinner operatorSelector;

    private TextView calcResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_calc); //このActivityで使われるlayoutファイルを指定する


        /*文字入力イベント受け取り*/
        numInput1 = (EditText) findViewById(R.id.numInput1);
        numInput1.addTextChangedListener(this);

        numInput2 = (EditText)findViewById(R.id.numInput2);
        numInput2.addTextChangedListener(this);


        operatorSelector = (Spinner)findViewById(R.id.operatorSelector);
        calcResult = (TextView)findViewById(R.id.calcResult);


        /*ボタンのリスナークラスを登録*/
        findViewById(R.id.backScene).setOnClickListener(this);
    }

    @Override //テキストが変更される直前に呼ばれる。sは変更前の内容
    public void beforeTextChanged(CharSequence s, int start, int count, int after){

    }

    @Override //テキストが変更されるときに呼ばれる。sは変更後の内容で編集不可能
    public void onTextChanged(CharSequence s, int start, int before, int count){

    }

    @Override //テキストが変更された後に呼ばれる。sは変更後の内容で編集可能
    public void afterTextChanged(Editable s){
        refreshResult();
    }



    @Override
    public void onClick(View v){
        if(!checkEditTextInput()){
            setResult(RESULT_CANCELED);
        }else{
            int result = calc();

            Intent data = new Intent();
            data.putExtra("result", result);
            setResult(RESULT_OK, data);
        }

        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;

        Bundle resultBundle = data.getExtras();
        if(!resultBundle.containsKey("result")) return;
        int result = resultBundle.getInt("result");


        if(requestCode == REQUEST_CODE_ANOTHER_CALC_1){
            numInput1.setText(String.valueOf(result));
        }else if(requestCode == REQUEST_CODE_ANOTHER_CALC_2){
            numInput2.setText(String.valueOf(result));
        }

        refreshResult();
    }



    private void refreshResult(){
        if(checkEditTextInput()){
            int result = calc();

            String resultText = getString(R.string.calcResult, result); //resultの数値が、calcResultの書式になって返る
            calcResult.setText(resultText);
        }else{ //両方の入力が終了していない場合、計算結果をデフォルトに戻す
            calcResult.setText(R.string.calcResultDefault);
        }
    }

    private boolean checkEditTextInput(){
        String input1 = numInput1.getText().toString();
        String input2 = numInput2.getText().toString();

        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }


    private int calc(){
        String input1 = numInput1.getText().toString();
        String input2 = numInput2.getText().toString();

        int num1 = Integer.parseInt(input1);
        int num2 = Integer.parseInt(input2);

        //Spinnerから、選択中のindexを取得する
        int operator = operatorSelector.getSelectedItemPosition();

        switch(operator){
            case 0:
                return num1 + num2;
            case 1:
                return num1 - num2;
            case 2:
                return num1 * num2;
            case 3:
                return num1 / num2;
            default:
                throw new RuntimeException();
        }
    }


}

