package xyz.rnovoselov.photon.ui.screens.splash;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import xyz.rnovoselov.photon.R;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.mvp.views.AbstractView;
import xyz.rnovoselov.photon.ui.screens.splash.SplashScreen;

/**
 * Created by roman on 29.06.17.
 */

public class SplashView extends AbstractView<SplashScreen.SplashPresenter> {

    @BindView(R.id.btn_next)
    Button next;

    public SplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<SplashScreen.Component>getDaggerComponent(context).inject(this);
    }

    @OnClick(R.id.btn_next)
    void onNextClick () {
        mPresenter.onNextClick();
    }

    @OnClick(R.id.btn_third)
    void onThitdClick() {
        mPresenter.onThirdClick();
    }
}
