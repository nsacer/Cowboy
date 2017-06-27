package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.live9666.cowboy.R;
import cn.live9666.cowboy.WebViewActivity;
import model.AdModel;

/**
 * Created by zpf on 2016/11/2.
 * 广告轮播使用的Viewpager Adapter
 */
public class AdViewPagerAdapter extends PagerAdapter implements View.OnClickListener{

    private Context mContext;
    private ArrayList<AdModel> models = new ArrayList<>();
    private Picasso picasso;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public AdViewPagerAdapter(Context context) {

        this.mContext = context;
        picasso = Picasso.with(context);
        inflater = LayoutInflater.from(context);
    }
    public AdViewPagerAdapter(Context context, ArrayList<AdModel> models) {

        this.mContext = context;
        picasso = Picasso.with(context);
        inflater = LayoutInflater.from(context);
        if(models != null && !models.isEmpty())
            this.models = models;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setArrayList(ArrayList<AdModel> adModels) {

        if(adModels != null && !adModels.isEmpty()) {

            this.models.clear();
            this.models = adModels;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {

        if(onItemClickListener != null)
            onItemClickListener.OnItemClick(view, (AdModel) view.getTag());
    }

    public interface OnItemClickListener{

        void OnItemClick(View view, AdModel model);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.iv_m_m, container, false);
        view.setOnClickListener(this);

        ImageView iv = (ImageView) view.findViewById(R.id.iv_m_m);
        AdModel model = models.get(position);
        view.setTag(model);

        if(!TextUtils.isEmpty(model.getImg())) {

            picasso.load((model.getImg()))
                    .placeholder(R.mipmap.holder)
                    .error(R.mipmap.holder)
                    .into(iv);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
