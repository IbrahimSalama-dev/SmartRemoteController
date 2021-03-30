package com.example.smartremotecontroller;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private String[] localDataSet;
    private boolean isDevice;
    private SelectionTracker<Long> tracker = null;
    private MyViewModel model;
    private String deviceName;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btn;
        private boolean isDevice;
        private Context context;
        private MyViewModel model;
        private String deviceName;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            btn = (Button) view.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isDevice){
                        context = view.getContext();
                        Intent intent= new Intent(context, ButtonsActivity.class);
                        String deviceName = (String) btn.getText();
                        intent.putExtra("deviceName",deviceName);
                        context.startActivity(intent);
                    }
                    else {
                        model.performButtonFunction((String)btn.getText(), deviceName);
                    }
                }
            });
        }

        public Button getButton() {
            return btn;
        }

        public void setModel(MyViewModel model){
            this.model = model;
        }

        public void setDeviceName(String deviceName){
            this.deviceName = deviceName;
        }

        public void setDevice(boolean device) {
            isDevice = device;
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails(){
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Nullable
                @Override
                public Long getSelectionKey() {
                    return getItemId();
                }
            };
        }
    }

    public void setTracker(SelectionTracker<Long> tracker) {
        this.tracker = tracker;
    }

    public void setLocalDataSet(String[] localDataSet) {
        this.localDataSet = localDataSet;
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(String[] dataSet, boolean isDevice) {
        localDataSet = dataSet;
        this.isDevice = isDevice;
        setHasStableIds(true);
    }

    public void setModel(MyViewModel model) {
        this.model = model;
    }
    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setDevice(isDevice);
        viewHolder.setModel(model);
        viewHolder.setDeviceName(deviceName);
        viewHolder.getButton().setText(localDataSet[position]);
        View parent = (View)viewHolder.getButton().getParent();
        if(tracker.isSelected((long)position)) {
            parent.setBackgroundColor(Color.parseColor("#80deea"));
            viewHolder.getButton().setEnabled(false);
        } else {
            // Reset color to white if not selected
            parent.setBackgroundColor(Color.WHITE);
            viewHolder.getButton().setEnabled(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    @Override
    public long getItemId(int position) {
        //return super.getItemId(position);
        return (long)position;
    }
}
