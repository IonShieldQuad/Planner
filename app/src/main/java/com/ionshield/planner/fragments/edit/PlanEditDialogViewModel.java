package com.ionshield.planner.fragments.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.PlanDao;
import com.ionshield.planner.database.entities.Plan;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlanEditDialogViewModel extends ViewModel {
    private final MutableLiveData<Plan> plan = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public PlanEditDialogViewModel() {
        plan.setValue(new Plan());
    }

    public void setPlanById(long id) {
        ListenableFuture<List<Plan>> res = PlannerRepository.getInstance().getPlanDao().queryPlans(id);
        res.addListener(() -> {
            try {
                List<Plan> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (plan) {
                        plan.postValue(list.get(0));
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writePlan() {
        PlanDao dao = PlannerRepository.getInstance().getPlanDao();
        Plan value = plan.getValue();
        if (value != null) {
            if (value.planId == 0) {
                dao.insertAll(value);
            }
            else {
                dao.updateAll(value);
            }
        }
    }

    public LiveData<Plan> getPlan() {
        return plan;
    }

    public void setName(String name) {
        if (plan.getValue() != null) {
            synchronized (plan) {
                plan.getValue().name = name;
                plan.setValue(plan.getValue());
            }
        }
    }

    public void setDescription(String description) {
        if (plan.getValue() != null) {
            synchronized (plan) {
                plan.getValue().description = description;
                plan.setValue(plan.getValue());
            }
        }
    }
}
