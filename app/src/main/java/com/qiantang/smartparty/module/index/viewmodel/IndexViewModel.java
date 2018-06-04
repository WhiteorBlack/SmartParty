package com.qiantang.smartparty.module.index.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxIndexClass;
import com.qiantang.smartparty.modle.RxIndexNews;
import com.qiantang.smartparty.modle.RxVideoStudy;
import com.qiantang.smartparty.module.index.adapter.NewsAdapter;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class IndexViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private NewsAdapter newsAdapter;

    public IndexViewModel(BaseBindFragment fragment, NewsAdapter newsAdapter) {
        this.fragment = fragment;
        this.newsAdapter = newsAdapter;
    }

    public List<RxIndexClass> getClassData() {
        List<RxIndexClass> classes = new ArrayList<>();
        classes.add(new RxIndexClass("视频学习", R.mipmap.icon_video_study, 0));
        classes.add(new RxIndexClass("系列讲话", R.mipmap.icon_speech, 1));
        classes.add(new RxIndexClass("专题学习", R.mipmap.icon_class_study, 2));
        classes.add(new RxIndexClass("考试评测", R.mipmap.icon_test, 3));
        classes.add(new RxIndexClass("学习排行", R.mipmap.icon_study_rank, 4));
        classes.add(new RxIndexClass("理论在线", R.mipmap.icon_online, 5));
        classes.add(new RxIndexClass("先进典范", R.mipmap.icon_top_man, 6));
        classes.add(new RxIndexClass("好书推荐", R.mipmap.icon_nice_book, 7));
        return classes;
    }

    /**
     * 学习动态模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener studyStateToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        };
    }

    /**
     * 学习视频模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener studyVideoToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        };
    }

    /**
     * 系列讲话模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener speechToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        };
    }

    /**
     * 两学一做模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener studyProToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        };
    }

    /**
     * 党章党规模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener rulesToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        };
    }

    /**
     * 新闻快报模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener newsToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        };
    }

    /**
     * 分类模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener classToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int pos = ((RxIndexClass) adapter.getData().get(position)).pos;
                switch (pos) {
                    case 0:
                        ActivityUtil.startVideoStudyActivity(fragment.getActivity());
                        break;
                    case 1:
                        ActivityUtil.startSpeechStudyActivity(fragment.getActivity());
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4://学习排行
                        ActivityUtil.startRankActivity(fragment.getActivity());
                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;

                }
            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_news_more:
                //新闻 更多
                ActivityUtil.startNewsActivity(fragment.getActivity());
                break;
            case R.id.tv_study_state_more:
                //学习动态
                ActivityUtil.startStudyStateActivity(fragment.getActivity());
                break;
            case R.id.tv_study_video:
                //学习视频
                ActivityUtil.startVideoStudyActivity(fragment.getActivity());
                break;
            case R.id.tv_study_practice:
                //两学一做

                break;
            case R.id.tv_speech:
                //系列讲话
                ActivityUtil.startSpeechStudyActivity(fragment.getActivity());
                break;
            case R.id.tv_rules:
                //党章党规

                break;
        }
    }

    @Override
    public void destroy() {

    }

    public void testNew() {
        List<RxIndexNews> news = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                RxIndexNews topNews = new RxIndexNews(RxIndexNews.ITEM_TOP);
                topNews.setPicUrl("http://pic.qiantucdn.com/58pic/14/78/41/77358PICZaq_1024.jpg");
                news.add(topNews);
            } else {
                RxIndexNews bottomNews = new RxIndexNews(RxIndexNews.ITEM_BOTTOM);
                bottomNews.setPicUrl("http://pic.qiantucdn.com/58pic/14/78/41/77358PICZaq_1024.jpg");
                news.add(bottomNews);
            }
        }
        newsAdapter.setNewData(news);
    }

    public List<RxVideoStudy> testData() {
        List<RxVideoStudy> videoStudies = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            RxVideoStudy rxVideoStudy = new RxVideoStudy();
            rxVideoStudy.setPicUrl("http://pic.qiantucdn.com/58pic/14/78/41/77358PICZaq_1024.jpg");
            videoStudies.add(rxVideoStudy);
        }
        return videoStudies;
    }
}
