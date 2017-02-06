package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.mobithink.carbon.R;

/**
 * Created by mplaton on 31/01/2017.
 */

public class CreateLineActivity extends Activity {

    TextInputLayout mWriteLineTextInputLayout;
    TextInputLayout mWriteCityNameTextInputLayout;
    TextInputLayout mWriteDepartureStationTextInputLayout;
    TextInputLayout mAddFirstStationTextInputLayout;
    TextInputLayout mAddSecondStationTextInputLayout;

    TextInputEditText mWriteLineTextInputEditText;
    TextInputEditText mWriteCityNameTextInputEditText;
    TextInputEditText mWriteDepartureStationTextInputEditText;
    TextInputEditText mAddFirstStationTextInputEditText;
    TextInputEditText mAddSecondStationTextInputEditText;

    Button mCreateLineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_line);

        mWriteLineTextInputLayout = (TextInputLayout) findViewById(R.id.Line_Name_TextInputLayout);
        mWriteCityNameTextInputLayout = (TextInputLayout) findViewById(R.id.City_Name_TextInputLayout);
        mWriteDepartureStationTextInputLayout = (TextInputLayout) findViewById(R.id.Departure_Station_TextInputLayout);
        mAddFirstStationTextInputLayout = (TextInputLayout) findViewById(R.id.Adding_Station_TextInputLayout);
        mAddSecondStationTextInputLayout = (TextInputLayout) findViewById(R.id.Adding_Second_Station_TextInputLayout);

        mWriteLineTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Line_Name);
        mWriteCityNameTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_City_Name);
        mWriteDepartureStationTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Departure_Station);
        mAddFirstStationTextInputEditText = (TextInputEditText) findViewById(R.id.Adding_Station);
        mAddSecondStationTextInputEditText = (TextInputEditText) findViewById(R.id.Adding_second_Station);

        mCreateLineButton = (Button) findViewById(R.id.createLine);

        mCreateLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLine();
            }
        });
    }

    public void createLine(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Confirmer les stations");
        alertDialogBuilder.setMessage("");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
