package com.swust.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.swust.R;
import com.swust.adapter.UnitAdapter;
import com.swust.db.dao.UnitDao;
import com.swust.pojo.Unit;
import com.swust.utils.Const;
import com.swust.utils.DateUtils;
import com.swust.utils.PlaySoundUtils;

import java.util.List;

public class UnitListFgt extends Fragment implements AdapterView.OnItemClickListener {

    private int mCurUnit = 0;
    private View mClickedView;
    private UnitDao mUnitDao;
    private onUnitClickListener mOnUnitClickListener;

    public static UnitListFgt getInstance(String metaKey) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.META_KEY, metaKey);
        UnitListFgt unitListFgt = new UnitListFgt();
        unitListFgt.setArguments(bundle);
        return unitListFgt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onUnitClickListener) {
            mOnUnitClickListener = (onUnitClickListener) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCurUnit != 0 && mClickedView != null) {
            long curTime = mUnitDao.getTime(getArguments().getString(Const.META_KEY),
                    mCurUnit);
            TextView textView = mClickedView.findViewById(R.id.tv_time);
            String text = getString(R.string.sum_time) + DateUtils.formatTime(curTime);
            textView.setText(text);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        //QuickScroll quickScroll = view.findViewById(R.id.quickscroll);
        ListView listView = view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        /*listView.setDivider(getResources().getDrawable(R.mipmap.word_line));
        listView.setDividerHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getActivity().getResources().getDisplayMetrics()));*/
        mUnitDao = new UnitDao(getActivity());
        List<Unit> units = mUnitDao.getUnits(getArguments().getString(Const.META_KEY));
        UnitAdapter unitAdapter = new UnitAdapter(getActivity(), units);

        listView.setAdapter(unitAdapter);
        /*quickScroll.init(QuickScroll.TYPE_POPUP_WITH_HANDLE, listView, unitAdapter, QuickScroll.STYLE_HOLO);
        quickScroll.setFixedSize(2);

        quickScroll.setPopupColor(QuickScroll.BLUE_LIGHT, QuickScroll.BLUE_LIGHT_SEMITRANSPARENT, 1, Color.WHITE, 1);
        quickScroll.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 42);*/
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnUnitClickListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurUnit = position + 1;
        PlaySoundUtils.playSound(getContext());
        mClickedView = view;
        if (mOnUnitClickListener != null) {
            mOnUnitClickListener.getUnit((Unit) parent.getItemAtPosition(position));
        }
    }

    public interface onUnitClickListener {
        void getUnit(Unit unit);
    }
}