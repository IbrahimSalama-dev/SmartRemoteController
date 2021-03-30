package com.example.smartremotecontroller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Iterator;

public class ButtonsActivity extends AppCompatActivity {
    private String deviceName;
    private FloatingActionButton addButtonButton;
    private String []buttonsList = {""};
    private MyViewModel model;
    private Toolbar toolbar = null;
    private Menu menu;
    private String[] selectedItems;
    private MenuItem remove;
    private MenuItem edit;
    private SelectionTracker tracker;
    private RecyclerView r;
    private CustomAdapter adapter;
    private boolean selected = false;
    private int code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);
        showOverflowMenu(false);
        deviceName = getIntent().getStringExtra("deviceName");
        getSupportActionBar().setTitle(deviceName);
        r = findViewById(R.id.DeviceList);
        adapter = new CustomAdapter(buttonsList, false);
        model = ViewModelProviders.of(this).get(MyViewModel.class);
        adapter.setDeviceName(deviceName);
        adapter.setModel(model);
        model.getData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                Log.d("dbupdate", "onChanged entered ");
                if (user != null) {
                    buttonsList = user.getDevicesList().get(deviceName).getButtonsList().keySet().toArray(new String[user.getDevicesList().get(deviceName).getButtonsList().size()]);
                    adapter.setLocalDataSet(buttonsList);
                    if (buttonsList.length > 0) {
                        r.setAdapter(adapter);
                        if(tracker==null)
                            tracker = new SelectionTracker.Builder<Long>(
                                    "my-selection-id",
                                    r,
                                    new StableIdKeyProvider(r),
                                    new MyDetailsLookup(r),
                                    StorageStrategy.createLongStorage())
                                    .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                                    .build();
                        adapter.setTracker(tracker);
                        tracker.addObserver(new SelectionTracker.SelectionObserver<Long>(){
                            @Override
                            public void onSelectionChanged(){
                                int numItems = tracker.getSelection().size();
                                if(numItems == 1){
                                    edit.setEnabled(true);
                                    selected = true;
                                    showOverflowMenu(true);
                                    getSupportActionBar().setTitle(numItems+" items selected");
                                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable((Color.parseColor("#ef6c00"))));
                                }
                                else if(numItems > 0) {
                                    edit.setEnabled(false);
                                    selected = true;
                                    // Change title and color of action bar
                                    showOverflowMenu(true);
                                    getSupportActionBar().setTitle(numItems+" items selected");
                                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable((Color.parseColor("#ef6c00"))));
                                } else {
                                    selected = false;
                                    showOverflowMenu(false);
                                    // Reset color and title to default values
                                    getSupportActionBar().setTitle(deviceName);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.design_default_color_primary)));
                                    }
                                }
                                Log.d("mont", "onSelectionChanged: exit");
                            }
                        });

                    }
                    adapter.notifyItemChanged(0, buttonsList.length);
                    adapter.notifyDataSetChanged();

                }
            }
        });
        addButtonButton =  findViewById(R.id.floatingActionButton);
        addButtonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getButtonName();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        remove = menu.findItem(R.id.Remove).setEnabled(true);
        edit = menu.findItem(R.id.Edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Remove ) {
            int numItems = tracker.getSelection().size();
            selectedItems = new String[numItems];
            Iterator itr = tracker.getSelection().iterator();
            for (int i = 0; i < numItems; i++) {
                Long l = (Long) itr.next();
                FrameLayout f = (FrameLayout) r.getChildAt(l.intValue());
                Button b = (Button) f.getChildAt(0);
                selectedItems[i] = b.getText().toString();
            }
            tracker.clearSelection();
            for (int i = 0; i < selectedItems.length; i++)
            if(MainActivity.isInternetAvailable())
                model.removeButtons(selectedItems, deviceName);
            else
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection, Please make sure you are connected to internet", Toast.LENGTH_LONG).show();
            selectedItems = null;
            tracker.clearSelection();
        }
        else if(item.getItemId() == R.id.Edit){
            Iterator itr = tracker.getSelection().iterator();
            Long l = (Long) itr.next();
            FrameLayout f = (FrameLayout) r.getChildAt(l.intValue());
            Button b = (Button) f.getChildAt(0);
            String selectedDeviceName = b.getText().toString();;
            AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(this);
            alertDialogueBuilder.setTitle("Please Enter The New Button Name");
            // Set up the input
            final EditText input = new EditText(getBaseContext());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            alertDialogueBuilder.setView(input);

            // Set up the buttons
            alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String buttonName = input.getText().toString();
                    if (((buttonName.contains("/")|| buttonName.contains(".")|| buttonName.contains("#")
                            || buttonName.contains("$")||buttonName.contains("[") || buttonName.contains("]")
                            || buttonName.equals("")))) {
                        Toast.makeText(getApplicationContext(), "Name must not contain special characters or an empty name",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(MainActivity.isInternetAvailable())
                        model.changeButtonName(buttonName, selectedDeviceName, deviceName);
                    else
                        Toast.makeText(getApplicationContext(),
                                "No Internet Connection, Please make sure you are connected to internet", Toast.LENGTH_LONG).show();
                    tracker.clearSelection();
                }
            });
            alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialogueBuilder.show();
        }
        return true;
    }

    public void showOverflowMenu(boolean showMenu){
        if(menu == null)
            return;
        menu.setGroupVisible(R.id.main_menu_group, showMenu);
    }

    @Override
    public void onBackPressed() {
        if (selected)
            tracker.clearSelection();
        else
            finish();
    }

    public void getButtonName(){
        final EditText input = new EditText(getBaseContext());
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(input)
                .setTitle("please enter button name")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        boolean isFinished = false;
                        String buttonName = input.getText().toString();
                        if (((buttonName.contains("/")|| buttonName.contains(".")|| buttonName.contains("#")
                                || buttonName.contains("$")||buttonName.contains("[") || buttonName.contains("]")
                                || buttonName.equals("")))) {
                            Toast.makeText(getApplicationContext(), "Name must not contain special characters or an empty name",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!MainActivity.isInternetAvailable()){
                            Toast.makeText(getApplicationContext(),
                                    "No Internet Connection, Please make sure you are connected to internet", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "Please press the button you want to enter from your remote controller to save it",
                                Toast.LENGTH_LONG).show();

                        ProgressDialog myDialog = new ProgressDialog(dialog.getContext());
                        myDialog.setMessage("Please press the button you want to add from your remote controller.");

                        model.getLiveCode().observe(ButtonsActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String string) {
                                code = Integer.parseInt(string);
                                if (code != 0) {
                                    Toast.makeText(getApplicationContext(), "code read", Toast.LENGTH_LONG).show();
                                    model.addButton(buttonName, deviceName, code);
                                    Toast.makeText(getApplicationContext(), "Button Saved!!", Toast.LENGTH_LONG).show();
                                    myDialog.dismiss();
                                    dialog.dismiss();
                                    return;
                                }
                            }
                        });
                        myDialog.show();
                    }
                });
            }
        });
        dialog.show();
    }

}
