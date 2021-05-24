package com.ionshield.planner.fragments.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.ConstraintDao;
import com.ionshield.planner.database.entities.Constraint;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConstraintEditDialogViewModel extends ViewModel {
    private final MutableLiveData<Constraint> constraint = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ConstraintEditDialogViewModel() {
        constraint.setValue(new Constraint());
    }

    public void setConstraintById(long id, Long planId) {
        ListenableFuture<List<Constraint>> res = PlannerRepository.getInstance().getConstraintDao().queryConstraints(id);
        res.addListener(() -> {
            try {
                List<Constraint> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (constraint) {
                        if (planId != null) {
                            list.get(0).planId = planId;
                        }
                        constraint.postValue(list.get(0));
                    }
                }
                else {
                    Constraint n = new Constraint();
                    if (planId != null) {
                        n.planId = planId;
                    }
                    constraint.postValue(n);

                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writeConstraint() {
        ConstraintDao dao = PlannerRepository.getInstance().getConstraintDao();
        Constraint value = constraint.getValue();
        if (value != null) {
            if (value.constraintId == 0) {
                dao.insertAll(value);
            }
            else {
                dao.updateAll(value);
            }
        }
    }

    public LiveData<Constraint> getConstraint() {
        return constraint;
    }

    public void setName(String name) {
        if (constraint.getValue() != null) {
            synchronized (constraint) {
                constraint.getValue().name = name;
                constraint.setValue(constraint.getValue());
            }
        }
    }

    public void setContent(String content) {
        if (constraint.getValue() != null) {
            synchronized (constraint) {
                constraint.getValue().content = content;
                constraint.setValue(constraint.getValue());
            }
        }
    }
}
