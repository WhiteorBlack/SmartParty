package com.qiantang.smartparty.module.index.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxIndex;
import com.qiantang.smartparty.modle.RxIndexClass;
import com.qiantang.smartparty.modle.RxIndexNews;
import com.qiantang.smartparty.modle.RxIndexSection;
import com.qiantang.smartparty.modle.RxIndexStudy;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.adapter.IndexSectionAdapter;
import com.qiantang.smartparty.module.index.adapter.IndexVideoAdapter;
import com.qiantang.smartparty.module.index.adapter.NewsAdapter;
import com.qiantang.smartparty.module.index.adapter.SpechAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class IndexViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private NewsAdapter newsAdapter;
    private IndexCommonAdapter studyAdapter;
    private IndexVideoAdapter videoStudyAdapter;
    private SpechAdapter spechAdapter;
    private IndexSectionAdapter sectionAdapter;
    private List<RxIndexSection> sectionList = new ArrayList<>();

    public IndexViewModel(BaseBindFragment fragment, NewsAdapter newsAdapter) {
        this.fragment = fragment;
        this.newsAdapter = newsAdapter;
    }

    public void setAdater(NewsAdapter newsAdapter, IndexCommonAdapter studyAdapter, IndexVideoAdapter videoStudyAdapter, SpechAdapter spechAdapter, IndexSectionAdapter sectionAdapter) {
        this.newsAdapter = newsAdapter;
        this.studyAdapter = studyAdapter;
        this.spechAdapter = spechAdapter;
        this.videoStudyAdapter = videoStudyAdapter;
        this.sectionAdapter = sectionAdapter;
    }

    public void getData() {
        ApiWrapper.getInstance().showHomePage()
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxIndex>>() {
                    @Override
                    public void onSuccess(List<RxIndex> data) {
                        if (data.get(0) != null) {
                            List<RxIndexNews> newsList = data.get(0).getNews();
                            for (int i = 0; i < newsList.size(); i++) {
                                if (i == 0) {
                                    newsList.get(i).setItemType(RxIndexNews.ITEM_TOP);
                                } else {
                                    newsList.get(i).setItemType(RxIndexNews.ITEM_BOTTOM);
                                }
                            }
                            newsAdapter.setNewData(newsList);
                        }
                        if (data.get(1) != null) {
                            studyAdapter.setNewData(data.get(1).getStudy());
                        }
                        if (data.get(2) != null) {
                            videoStudyAdapter.setNewData(data.get(2).getVideo());
                        }
                        if (data.get(3) != null) {
                            spechAdapter.setNewData(data.get(3).getSpeak());
                        }
                        if (data.size() > 4) {
                            int lastId = 0;
                            for (int i = 0; i < data.size() - 4; i++) {
                                List<RxIndexStudy> studyList = data.get(4 + i).getContent();
                                if (studyList != null && studyList.size() > 0) {
                                    int classifyId = 0;
                                    if (i != 0) {
                                        classifyId = data.get(3 + i).getClassifyId();
                                    }
                                    classifyId = data.get(4 + i).getClassifyId();
                                    sectionList.add(new RxIndexSection(true, data.get(4 + i).getTitle(), i == 0, false, classifyId));
                                    for (int j = 0; j < studyList.size(); j++) {
                                        RxIndexSection rxIndexSection = new RxIndexSection(false);
                                        rxIndexSection.setRxIndexStudy(studyList.get(j));
                                        sectionList.add(rxIndexSection);
                                    }
                                }
                            }
                            if (sectionList.size() > 0) {
                                sectionList.add(new RxIndexSection(true, "", false, true, lastId));
                            }
                        }

                        if (sectionList.size() > 0) {
                            sectionAdapter.setNewData(sectionList);
                        }
                    }
                });
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
                ActivityUtil.startHeadWebActivity(fragment.getActivity(), studyAdapter.getData().get(position).getContentId(), "学习动态");
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
     * 底部部分
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener studyProToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.tv_speech:
                        int id = (Integer) view.getTag();
                        ToastUtil.toast(id + "");
                        break;
                }
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
                ActivityUtil.startHeadWebActivity(fragment.getActivity(), newsAdapter.getData().get(position).getContentId(), "新闻快报");
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
                ActivityUtil.startNewsActivity(fragment.getActivity(), 8);
                break;
            case R.id.tv_study_state_more:
                //学习动态
                ActivityUtil.startNewsActivity(fragment.getActivity(), 9);
                break;
            case R.id.tv_study_video:
                //学习视频
                ActivityUtil.startVideoStudyActivity(fragment.getActivity());
                break;
            case R.id.tv_speech:
                //系列讲话
                ActivityUtil.startSpeechStudyActivity(fragment.getActivity());
                break;
        }
    }

    @Override
    public void destroy() {

    }
}
