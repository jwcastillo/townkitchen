package awsomethree.com.townkitchen.adapters;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import awsomethree.com.townkitchen.R;
import awsomethree.com.townkitchen.models.Order;
import awsomethree.com.townkitchen.models.OrderLineItem;
import awsomethree.com.townkitchen.models.ShoppingCart;

/**
 * Created by ktruong on 3/29/15.
 */
public class ShoppingCartAdapter extends ArrayAdapter<OrderLineItem> {

    private class ViewHolder {
        public ImageView imageUrl;
        public TextView shippingDay;
        public TextView optionName;
        public TextView optionDesc;
    }

    private final ShoppingCart shoppingCart;

    public ShoppingCartAdapter(Context context, ShoppingCart shoppingCart) {
        super(context, 0, shoppingCart.getItems());
        this.shoppingCart = shoppingCart;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OrderLineItem orderLineItem = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_cart_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageUrl = (ImageView) convertView.findViewById(R.id.ivFoodImageUrl);
            viewHolder.shippingDay = (TextView) convertView.findViewById(R.id.tvShippingDay);
            viewHolder.optionName = (TextView) convertView.findViewById(R.id.tvOptionName);
            viewHolder.optionDesc = (TextView) convertView.findViewById(R.id.tvOptionDesc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageUrl.setImageResource(0);
        Picasso.with(getContext()).load(orderLineItem.getMenu().getImageUrl()).fit().into(viewHolder.imageUrl);

        Order order = orderLineItem.getOrder();
        Date shipDate = order.getShipDate();
        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(shipDate.getTime(), System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_WEEKDAY);
        viewHolder.shippingDay.setText(relativeTimeSpanString);
        viewHolder.optionName.setText(orderLineItem.getMenu().getName());
        viewHolder.optionDesc.setText(Html.fromHtml(orderLineItem.getMenu().getDescription()));

        return convertView;
    }
}