import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.pc.testeverything.R;
import com.example.pc.testeverything.Recyclerview.OrderDetail;

import java.util.List;

/**
 * Created by PC on 2018/8/4.
 */

public class ListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    List<OrderDetail> orderDetailList;
    Context context;

    public ListAdapter(List<OrderDetail> orderDetailList, Context context) {
        this.orderDetailList = orderDetailList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);

        }
        return null;
    }
}
