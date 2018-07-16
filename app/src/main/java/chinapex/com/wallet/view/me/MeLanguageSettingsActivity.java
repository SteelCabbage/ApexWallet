package chinapex.com.wallet.view.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.LanguageRecyclerViewAdapter;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.bean.LanguageState;

public class MeLanguageSettingsActivity extends BaseActivity implements
        LanguageRecyclerViewAdapter.OnItemClickListener {

    private LanguageRecyclerViewAdapter mLanguageRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_language_settings);

        initView();
    }

    private void initView() {
        RecyclerView rv_me_language = (RecyclerView) findViewById(R.id.rv_me_language_settings);
        rv_me_language.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mLanguageRecyclerViewAdapter = new LanguageRecyclerViewAdapter(getAssetsMenus());
        mLanguageRecyclerViewAdapter.setOnItemClickListener(this);
        rv_me_language.setAdapter(mLanguageRecyclerViewAdapter);
    }

    private List<LanguageState> getAssetsMenus() {
        ArrayList<LanguageState> languageStates = new ArrayList<>();
        String[] menuTexts = getResources().getStringArray(R.array.me_language_name);

        for (int i = 0; i < menuTexts.length; i++) {
            LanguageState languageState = new LanguageState();
            languageState.setLanguageName(menuTexts[i]);

            if (i == 0) {
                languageState.setChecked(true);
            } else {
                languageState.setChecked(false);
            }

            languageStates.add(languageState);
        }
        return languageStates;
    }

    @Override
    public void onItemClick(int position) {

    }
}
