package edu.umw.cpsc.donahue.james.assignmentthree;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LocationFragment extends ListFragment {

    private ItemSelectedListener itemSelectedListener;
    private boolean send;
    ArrayList<MyLocation> data;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity().getClass().getSimpleName().equals("SentActivity")) {
            send = true;
        } else {
            send = false;
        }

        data = DataManager.readFromFile(send, this.getContext());
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, data);
        this.setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //itemSelectedListener.itemSelected(position);
        Intent intent = new Intent(this.getContext(), MapsActivity.class);
        //String[] array = getResources().getStringArray(R.array.test_sent_values);
        MyLocation location = data.get(position);
        intent.putExtra("LAT", String.valueOf(location.getLatitude()));
        intent.putExtra("LONG", String.valueOf(location.getLongitude()));
        intent.putExtra("CONTACT", location.getName());
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            itemSelectedListener = (ItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ItemSelectedListener");
        }
    }
}
