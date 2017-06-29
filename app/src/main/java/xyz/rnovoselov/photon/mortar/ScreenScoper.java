package xyz.rnovoselov.photon.mortar;

import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mortar.MortarScope;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.flow.AbstractScreen;

/**
 * Created by roman on 30.11.16.
 */

public class ScreenScoper {

    private static final String TAG = ScreenScoper.class.getSimpleName();
    private static Map<String, MortarScope> scopeMap = new HashMap<>();

    public static MortarScope getScreenScope(AbstractScreen screen) {
        if (!scopeMap.containsKey(screen.getScopeName())) {
            Log.e(TAG, "getScreenScope: create new scope");
            return createScreenScope(screen);
        } else {
            Log.e(TAG, "getScreenScope: return screen scope");
            return scopeMap.get(screen.getScopeName());
        }
    }

    /**
     * Метод для регистрации созданной области видимости (добавляет его в статическую мапу).
     *
     * @param scope скоуп, который необходимо зарегистрировать.
     */
    public static void registerScope(MortarScope scope) {
        scopeMap.put(scope.getName(), scope);
    }

    /**
     * Метод необходим на случай, когда удаляется область видимости с дочерними областями видимости,
     * чтобы вычистить из мапы всю "ветку" удаленных областей видимости.
     */
    private static void cleanScopeMap() {
        Iterator<Map.Entry<String, MortarScope>> iterator = scopeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MortarScope> entry = iterator.next();
            if (entry.getValue().isDestroyed()) {
                iterator.remove();
            }
        }
    }

    /**
     * Метод для удаления области видимости.
     *
     * @param scopeName область видимости, которую необходимо удалить
     */
    public static void destroyScreenScope(String scopeName) {
        Log.e(TAG, "destroyScreenScope: " + scopeName);
        MortarScope mortarScope = scopeMap.get(scopeName);
//        if (mortarScope != null)
        mortarScope.destroy();
        cleanScopeMap();
    }

    /**
     * Метод для получения имени родительского для скрина скоупа.
     *
     * @param screen скрин, для которого необходимо получить название скоупа родителя.
     * @return название родительского скоупа.
     */
    @Nullable
    private static String getParentScopeName(AbstractScreen screen) {
        try {
            // Через рефлексию получаем дженерик, который передается в AbstractScreen,
            // например при создании SplashScreen передаем RootActivity.RootComponent
            String genericName = ((Class) ((ParameterizedType) screen.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0])
                    .getName();
            // Здесь хранится переданный дженерик в формате:
            // xyz.rnovoselov.photon.photon.ui.activities.RootActivity$RootComponent
            String parentScopeName = genericName;

            // Для корректности необходимо отбросить все, что за знаком $
            if (parentScopeName.contains("$")) {
                parentScopeName = parentScopeName.substring(0, genericName.indexOf("$"));
            }
            Log.e(TAG, "Parent scope name: " + parentScopeName);
            return parentScopeName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод для создания области видимости для заданного экрана (скрина).
     *
     * @param screen скрин, для которого необходмо создать область видимости.
     * @return созданная область видимости.
     */
    private static MortarScope createScreenScope(AbstractScreen screen) {
        Log.e(TAG, "ScreenScoper: create new scope with name - " + screen.getScopeName());
        // Сначала получаем родительский для скрина скоуп
        MortarScope parentScope = scopeMap.get(getParentScopeName(screen));
        // Получение родительского даггер компонента
        Object screenComponent = screen.createScreenComponent(parentScope.getService(DaggerService.SERVICE_NAME));
        // Создание нового скоупа
        MortarScope newScope = parentScope.buildChild()
                .withService(DaggerService.SERVICE_NAME, screenComponent)
                .build(screen.getScopeName());
        registerScope(newScope);
        return newScope;
    }
}
