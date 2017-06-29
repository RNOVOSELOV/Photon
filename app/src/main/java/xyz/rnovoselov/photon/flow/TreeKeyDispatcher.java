package xyz.rnovoselov.photon.flow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Collections;
import java.util.Map;

import flow.Direction;
import flow.Dispatcher;
import flow.KeyChanger;
import flow.State;
import flow.Traversal;
import flow.TraversalCallback;
import flow.TreeKey;
import xyz.rnovoselov.photon.R;
import xyz.rnovoselov.photon.mortar.ScreenScoper;
import xyz.rnovoselov.photon.utils.ViewHelper;


/**
 * Created by roman on 30.11.16.
 */

public class TreeKeyDispatcher implements Dispatcher, KeyChanger {

    private Activity mActivity;
    Object inKey;                           // Приходящий ключ (скрин, который должен появиться на экране)
    @Nullable
    private Object outKey;                  // Уходящий ключ (скрин, который уйдет с экрана)
    private FrameLayout mRootFrame;


    public TreeKeyDispatcher(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void dispatch(Traversal traversal, TraversalCallback callback) {
        Map<Object, Context> contexts;
        State inState = traversal.getState(traversal.destination.top());
        inKey = inState.getKey();
        State outState = (traversal.origin == null) ? null : traversal.getState(traversal.origin.top());
        outKey = (outState == null) ? null : outState.getKey();

        mRootFrame = ((FrameLayout) mActivity.findViewById(R.id.screen_container));
        if (inKey.equals(outKey)) {
            callback.onTraversalCompleted();
            return;
        }

        if (inKey instanceof TreeKey) {
            // TODO: 02.12.2016 implements treekey case
        }

        // TODO: 02.12.2016 mortar context for screen
        Context flowContext = traversal.createContext(inKey, mActivity);
        Context mortarContext = ScreenScoper.getScreenScope((AbstractScreen) inKey).createContext(flowContext);
        contexts = Collections.singletonMap(inKey, mortarContext);
        changeKey(outState, inState, traversal.direction, contexts, callback);
    }

    @Override
    public void changeKey(@Nullable State outgoingState, State incomingState, Direction direction, Map<Object, Context> incomingContexts, TraversalCallback callback) {

        Context context = incomingContexts.get(inKey);

        // Save prev View
        if (outgoingState != null) {
            outgoingState.save(mRootFrame.getChildAt(0));
        }

        // Create new View
        Screen screen;
        screen = inKey.getClass().getAnnotation(Screen.class);
        if (screen == null) {
            throw new IllegalStateException("@Screen annotoation is missing on screen " + ((AbstractScreen) inKey).getScopeName());
        } else {
            int layout = screen.value();
            LayoutInflater inflater = LayoutInflater.from(context);
            View newView = inflater.inflate(layout, mRootFrame, false);
            View oldView = mRootFrame.getChildAt(0);

            // Restore state to new View
            incomingState.restore(newView);

//            if (mRootFrame.getChildAt(0) != null) {
//                mRootFrame.removeView(mRootFrame.getChildAt(0));
//            }

            mRootFrame.addView(newView);

            ViewHelper.waitForMeasure(newView, new ViewHelper.OnMeasureCallback() { // Дожидаемся пока станут известны габариты вью
                @Override
                public void onMeasure(View view, int width, int height) {
                    runAnimation(mRootFrame, oldView, newView, direction, new TraversalCallback() { // запускаем анимацию
                        @Override
                        public void onTraversalCompleted() {    // анимация завершена, делаес то что необходимо
                            // Delete old View
                            if (outKey != null && !(inKey instanceof TreeKey)) {
                                ((AbstractScreen) outKey).unregisterScope();
                            }
                            callback.onTraversalCompleted();
                        }
                    });
                }
            });
        }
    }

    private void runAnimation(FrameLayout container, View from, View to, Direction direction, TraversalCallback callback) {
        Animator animator = createAnimation(from, to, direction);   // создаем анимацию
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (from != null) {
                    container.removeView(from);                     // удаляем вью из контейнера по окончании анимации
                }
                callback.onTraversalCompleted();                    // вызываем коллбэк успешного окончания перехода (в ктотором выше происходит очистка области видимости)
            }
        });
        animator.setInterpolator(new FastOutSlowInInterpolator());  // установка временной функции
        animator.start();                                           // запуск анимации
    }

    private Animator createAnimation(View from, View to, Direction direction) {
        boolean backward = direction == Direction.BACKWARD;

        AnimatorSet set = new AnimatorSet();

        int fromTranslation;
        if (from != null) {
            // если движесмя поистории назад, то смещаемся по Х = ширине вьюхи установленной в контейнере
            // если веперд то смещение отрицательное (справа на лево на ширину вью)
            fromTranslation = backward ? from.getWidth() : -from.getWidth();
            final ObjectAnimator outAnimation = ObjectAnimator.ofFloat(from, "translationX", fromTranslation);  // смещение по Х
            set.play(outAnimation);
        }

        // аналогично анимируем новую приходящую вью
        int toTranslation = backward ? -to.getWidth() : to.getWidth();
        final ObjectAnimator toAnimation = ObjectAnimator.ofFloat(to, "translationX", toTranslation, 0);
        set.play(toAnimation);
        return set;
    }
}
