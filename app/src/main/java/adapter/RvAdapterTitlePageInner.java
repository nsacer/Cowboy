package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.live9666.cowboy.R;
import customview.CircleImageView;
import model.IndexHotPointModel;

/**
 * Created by zpf on 2016/11/2.
 */
public class RvAdapterTitlePageInner extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener{

    private Context context;
    private ArrayList<IndexHotPointModel> modelList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View view) {

        if(onItemClickListener != null)
            onItemClickListener.OnItemClick(view, (IndexHotPointModel) view.getTag());
    }

    public interface OnItemClickListener{

        void OnItemClick(View view, IndexHotPointModel model);
    }

    public RvAdapterTitlePageInner(Context context) {

        this.context = context;
    }

    public void setModelList(List<IndexHotPointModel> models) {

        if (models != null) {
            modelList.clear();
            modelList.addAll(models);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inner_title_page, parent, false);
        view.setOnClickListener(this);
        return new HolderInner(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        IndexHotPointModel model = modelList.get(position);
        if (model == null)
            return;

        HolderInner holderInner = (HolderInner) holder;

        holderInner.itemView.setTag(model);

        Picasso.with(context).load(model.getUserPic())
                .config(Bitmap.Config.RGB_565).placeholder(R.mipmap.header)
                .error(R.mipmap.header).into(holderInner.ivHeader);

        holderInner.tvAuthor.setText(model.getNickName());

        holderInner.tvTitle.setText(model.getTitle());

        if (!TextUtils.isEmpty(model.getReadNum())) {

            String read = model.getReadNum().substring(0, model.getReadNum().length() - 2) +
                    " " + model.getReadNum().substring(model.getReadNum().length() - 2);
            holderInner.tvRead.setText(read);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    private class HolderInner extends RecyclerView.ViewHolder {

        CircleImageView ivHeader;
        TextView tvTitle, tvAuthor, tvRead;

        HolderInner(View itemView) {
            super(itemView);

            ivHeader = (CircleImageView) itemView.findViewById(R.id.civ_header_inner);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title_inner);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author_inner);
            tvRead = (TextView) itemView.findViewById(R.id.tv_read_inner);

        }
    }
}
