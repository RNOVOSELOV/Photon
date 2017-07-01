package xyz.rnovoselov.photon.ui.screens.third;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import xyz.rnovoselov.photon.R;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.mvp.views.AbstractView;

/**
 * Created by roman on 30.06.17.
 */

public class ThirdView extends AbstractView<ThirdScreen.ThirdPresenter> {

    @BindView(R.id.ident)
    TextView identificatorTv;

    public ThirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<ThirdScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void showData(int ident) {
        identificatorTv.setText(String.valueOf(ident));
    }
}
