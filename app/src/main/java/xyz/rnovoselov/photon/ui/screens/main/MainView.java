package xyz.rnovoselov.photon.ui.screens.main;

import android.content.Context;
import android.util.AttributeSet;

import butterknife.BindView;
import butterknife.OnClick;
import xyz.rnovoselov.photon.R;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.mvp.views.AbstractView;

/**
 * Created by roman on 30.06.17.
 */

public class MainView extends AbstractView<MainScreen.MainPresenter> {

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<MainScreen.Component>getDaggerComponent(context).inject(this);
    }

    @OnClick(R.id.btn_third)
    void onThirdClick(){
        mPresenter.onThirdClick();
    }
}
