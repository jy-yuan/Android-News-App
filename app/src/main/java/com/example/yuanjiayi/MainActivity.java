package com.example.yuanjiayi;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.yuanjiayi.api.LoadSaver;
import com.example.yuanjiayi.api.Recommender;
import com.example.yuanjiayi.api.Searcher;
import com.example.yuanjiayi.models.Article;
import com.example.yuanjiayi.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


//import androidx.appcompat.app.AlertController;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String username = "guest";
    private int Page = 1;
    private String mWords = "";
    private String mCategory = "";
    private TYPE type = TYPE.ALL;
    private List<String> blocklist = new ArrayList<>();
    private List<String> searchHistory = new ArrayList<>();
    private static boolean isBlocklistMode = false;
    private static boolean noSearch = false;
    private static boolean isNightMode = false;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private SearchHistoryAdapter searchHistoryAdapter;
    private CategoryAdapter categoryAdapter;
    private BlocklistAdapter blocklistAdapter;
    private String TAG = MainActivity.class.getSimpleName();

    private RefreshLayout mRefreshLayout;
    private SwipeRefreshLayout srl;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private View mImgMenu;
    private TextView myCategoryView;
    private Menu mMenu;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private FloatingActionButton fab;

    public static String[] categoryStr = {"娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"};
    public static int[] categoryInt = {R.drawable.ic_yule, R.drawable.ic_junshi, R.drawable.ic_jiaoyu, R.drawable.ic_wenhua,
            R.drawable.ic_jiankang, R.drawable.ic_caijing, R.drawable.ic_tiyu, R.drawable.ic_qiche, R.drawable.ic_keji, R.drawable.ic_shehui};
    public Boolean[] categoryBoolean = {true, true, true, true, true, true, true, true, true, true};
    public static int[] categoryId = {R.id.yule_entry, R.id.junshi_entry, R.id.jiaoyu_entry, R.id.wenhua_entry,
            R.id.jiankang_entry, R.id.caijing_entry, R.id.tiyu_entry, R.id.qiche_entry, R.id.keji_entry, R.id.shehui_entry};

    public enum TYPE {
        ALL,
        SUGGESTIONS,
        CATEGORY,
        HISTORY,
        FAVORITES,
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User.init(getApplicationContext());
        Searcher.context = getApplicationContext();

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Naïve News");
        setSupportActionBar(mToolbar);

        initDrawer();

        if(isNightMode){
            mNavigationView.getMenu().findItem(R.id.night_mode).setIcon(R.drawable.ic_sun);
            mNavigationView.getMenu().findItem(R.id.night_mode).setTitle("日间模式");
        } else {
            mNavigationView.getMenu().findItem(R.id.night_mode).setIcon(R.drawable.ic_moon);
            mNavigationView.getMenu().findItem(R.id.night_mode).setTitle("夜间模式");
        }

        mRefreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(!isBlocklistMode) {
                    LoadJson(type, false, mWords, mCategory);
                }
                refreshlayout.finishRefresh(800/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                LoadJson(type, true, mWords, mCategory);
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
            }
        });
        mRefreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        LoadJson(TYPE.ALL, false, "", "");

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearNews();
                mRefreshLayout.autoRefresh();
            }
        });
    }

    public void LoadJson(TYPE type, boolean isMore, String words, String category){
        mWords = words;
        if(isMore)
            Page++;
        else{
            articles.clear();
            Page = 1;
        }
        switch (type){
            case ALL:
                Searcher.Search(words, "", Page, new CallBack(this));
                break;
            case SUGGESTIONS:
                if(isMore)
                    Recommender.getnextRecommend(getApplicationContext(),new CallBack(this));
                else
                    Recommender.getlatestRecommend(getApplicationContext(), new CallBack(this));
                break;
            case CATEGORY:
                Searcher.Search(words,category, Page, new CallBack(this));
                break;
            case FAVORITES:
                articles = LoadSaver.Loadfav(getApplicationContext());
                adapter = new Adapter(articles, MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                initListener();
                break;
            case HISTORY:
                articles = LoadSaver.Loadnews(getApplicationContext());
                adapter = new Adapter(articles, MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                initListener();
                break;
        }
    }

    private void initListener(){

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(MainActivity.this, NewsDetailActivivty.class);

                Article article = articles.get(position);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("content", article.getContent());
                intent.putExtra("img",  article.getUrlToImage().get(0));
                intent.putExtra("imgUrls", article.getUrlToImage().toArray());
                intent.putExtra("date",  article.getTime().substring(0,10));
                intent.putExtra("source",  article.getPublishedAt());
                intent.putExtra("author",  article.getPersonList().get(0).getMention());
                intent.putExtra("isfav", article.getState() == Article.STATE.FAV ? true : false);
                intent.putExtra("position", position);
                intent.putExtra("video", article.getVideo());

                startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            int position = data.getIntExtra("Position", 0);
            boolean isfav = data.getBooleanExtra("Fav", false);
            Article thisarticle = articles.get(position);
            if(isfav){
                thisarticle.setState(Article.STATE.FAV);
            } else {
                thisarticle.setState(Article.STATE.HIS);
            }
            articles.set(position, thisarticle);
            LoadSaver.Savenews(thisarticle,getApplicationContext());
            adapter.notifyDataSetChanged();

        } else if(requestCode == 2 && resultCode == 2){
            for(int i = 0; i < 10; i++){
                categoryBoolean[i] = data.getBooleanExtra(((Integer) i).toString(), true);
            }
            setCategoryEntry();
            LoadSaver.saveCate(categoryBoolean,getApplicationContext());
        } else if(requestCode == 3 && requestCode == 3){
            if(data.getStringExtra("username").equals("")){
                return;
            }
            username = data.getStringExtra("username");
            if(username.equals("guest")){
                User.logout(getApplicationContext());
                ((TextView)mNavigationView.findViewById(R.id.username)).setText("登陆/注册");
            } else {
                User.login(username, getApplicationContext());
                ((TextView) mNavigationView.findViewById(R.id.username)).setText(username);
                blocklist = LoadSaver.Loadblock(getApplicationContext());
                categoryBoolean = LoadSaver.LoadCate(getApplicationContext());
                setCategoryEntry();
            }
            mNavigationView.getMenu().findItem(R.id.main_page).setChecked(true);
            mToolbar.setTitle("首页");
            mCategory = "";
            mWords = "";
            type = TYPE.ALL;
            mRefreshLayout.autoRefresh();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBlocklistMode){
                    searchView.setQueryHint("添加屏蔽关键词");
                } else {
                    searchView.setQueryHint("查查热点新闻");
                }
                if(!isBlocklistMode) {
                    searchHistoryAdapter = new SearchHistoryAdapter(searchHistory, MainActivity.this);
                    recyclerView.setAdapter(searchHistoryAdapter);
                    searchHistoryAdapter.notifyDataSetChanged();
                    searchHistoryAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String selectedHistory = searchHistory.get(position);
                            LoadJson(type, false, selectedHistory, mCategory);
                            searchView.clearFocus();
                            searchView.onActionViewCollapsed();
                            mRefreshLayout.autoRefresh();
                        }
                    });
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isBlocklistMode){
                    blocklist.add(query);
                    Searcher.blockList = blocklist;
                    LoadSaver.saveblock(blocklist, getApplicationContext());
                    blocklistAdapter.notifyDataSetChanged();
                } else {
                    searchHistoryAdapter = new SearchHistoryAdapter(new ArrayList<String>(), MainActivity.this);
                    recyclerView.setAdapter(searchHistoryAdapter);
                    searchHistoryAdapter.notifyDataSetChanged();
                    searchHistory.add(0, query);
                    if(noSearch) {
                        mNavigationView.getMenu().findItem(R.id.main_page).setChecked(true);
                        mToolbar.setTitle("首页");
                        mCategory = "";
                        type = TYPE.ALL;
                    }
                    mWords = query;
                    searchView.clearFocus();
                    searchView.onActionViewCollapsed();
                    mRefreshLayout.autoRefresh();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (!isBlocklistMode) {
                    LoadJson(TYPE.ALL, false, "", mCategory);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

            for(int i = 0; i < 10; i++) {
                intent.putExtra(((Integer) i).toString(), categoryBoolean[i]);
            }

            startActivityForResult(intent, 2);
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void initDrawer() {
        mDrawerLayout = findViewById(R.id.id_drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mNavigationView = findViewById(R.id.navigation_view);

        mNavigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        mNavigationView.inflateMenu(R.menu.navigation_drawer_menu);

        mNavigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.SendUInfo(getApplicationContext());
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle extras = ActivityOptions.makeCustomAnimation(MainActivity.this, android.R.anim.fade_in,
                        android.R.anim.fade_out).toBundle();
                Class activityClass = null;
                ClearNews();
                MainActivity.noSearch = false;
                mMenu.getItem(1).setVisible(false);
                if(item.getItemId() != R.id.block_list) {
                    fab.show();
                    MainActivity.isBlocklistMode = false;
                }
                switch(item.getItemId()){
                    case R.id.main_page:
                        mMenu.getItem(1).setVisible(true);
                        mToolbar.setTitle("首页");
                        type = TYPE.ALL;
                        mWords = "";
                        mCategory = "";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.suggest_page:
                        MainActivity.noSearch = true;
                        mMenu.getItem(1).setVisible(true);
                        mToolbar.setTitle("推荐");
                        type = TYPE.SUGGESTIONS;
                        mWords = "";
                        mCategory = "";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.fav_page:
                        MainActivity.noSearch = true;
                        mMenu.getItem(1).setVisible(true);
                        mToolbar.setTitle("收藏");
                        type = TYPE.FAVORITES;
                        mWords = "";
                        mCategory = "";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.history_page:
                        MainActivity.noSearch = true;
                        mMenu.getItem(1).setVisible(true);
                        mToolbar.setTitle("历史");
                        type = TYPE.HISTORY;
                        mWords = "";
                        mCategory = "";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.block_list:
                        mToolbar.setTitle("黑名单");
                        fab.hide();
                        MainActivity.isBlocklistMode = true;
                        mMenu.getItem(1).setVisible(false);
                        blocklistAdapter = new BlocklistAdapter(blocklist, MainActivity.this);
                        recyclerView.setAdapter(blocklistAdapter);
                        blocklistAdapter.notifyDataSetChanged();
                        blocklistAdapter.setOnItemClickListener(new BlocklistAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                blocklist.remove(position);
                                blocklistAdapter.notifyDataSetChanged();
                                Searcher.blockList = blocklist;
                            }
                        });
                        break;
                    case R.id.yule_entry:
                        mToolbar.setTitle("娱乐");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "娱乐";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.junshi_entry:
                        mToolbar.setTitle("军事");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "军事";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.jiaoyu_entry:
                        mToolbar.setTitle("教育");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "教育";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.wenhua_entry:
                        mToolbar.setTitle("文化");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "文化";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.jiankang_entry:
                        mToolbar.setTitle("健康");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "健康";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.caijing_entry:
                        mToolbar.setTitle("财经");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "财经";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.tiyu_entry:
                        mToolbar.setTitle("体育");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "体育";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.qiche_entry:
                        mToolbar.setTitle("汽车");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "汽车";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.keji_entry:
                        mToolbar.setTitle("科技");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "科技";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.shehui_entry:
                        mToolbar.setTitle("社会");
                        type = TYPE.CATEGORY;
                        mWords = "";
                        mCategory = "社会";
                        layoutManager.scrollToPosition(0);
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.night_mode:
                        if(MainActivity.isNightMode) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        MainActivity.isNightMode = !MainActivity.isNightMode;
                        recreate();
                        break;
                }

                //关闭侧滑菜单
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public class CallBack {
        private Context context;
        CallBack(Context context){
            this.context = context;
        }
        public void call(final List<Article> tmparticles){
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    articles.addAll(tmparticles);
                    adapter = new Adapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                }
            });
        }
    }

    public void ClearNews(){
        adapter = new Adapter(new ArrayList<Article>(), MainActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setCategoryEntry(){
        for(int i = 0; i < 10; i++) {
            mNavigationView.getMenu().findItem(categoryId[i]).setVisible(categoryBoolean[i]);
        }
    }
}
