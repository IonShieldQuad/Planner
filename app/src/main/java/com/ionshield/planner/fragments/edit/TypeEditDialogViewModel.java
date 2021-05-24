package com.ionshield.planner.fragments.edit;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.TypeDao;
import com.ionshield.planner.database.entities.Type;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TypeEditDialogViewModel extends ViewModel {
    private final MutableLiveData<Type> type = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TypeEditDialogViewModel() {
        type.setValue(new Type());
    }

    public void setTypeById(long id) {
        ListenableFuture<List<Type>> res = PlannerRepository.getInstance().getTypeDao().queryTypes(id);
        res.addListener(() -> {
            try {
                List<Type> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (type) {
                        type.postValue(list.get(0));
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writeType() {
        TypeDao dao = PlannerRepository.getInstance().getTypeDao();
        Type value = type.getValue();
        if (value != null) {
            if (value.typeId == 0) {
                dao.insertAll(value);
            }
            else {
                dao.updateAll(value);
            }
        }
    }

    public LiveData<Type> getType() {
        return type;
    }

    public void setName(String name) {
        if (type.getValue() != null) {
            synchronized (type) {
                type.getValue().name = name;
                type.setValue(type.getValue());
            }
        }
    }

    public void setDescription(String description) {
        if (type.getValue() != null) {
            synchronized (type) {
                type.getValue().description = description;
                type.setValue(type.getValue());
            }
        }
    }

    public void setRed(int red) {
        if (type.getValue() != null) {
            synchronized (type) {
                type.getValue().color = Color.argb(255, red, Color.green(type.getValue().color), Color.blue(type.getValue().color));
                type.setValue(type.getValue());
            }
        }
    }

    public void setGreen(int green) {
        if (type.getValue() != null) {
            synchronized (type) {
                type.getValue().color = Color.argb(255, Color.red(type.getValue().color), green, Color.blue(type.getValue().color));
                type.setValue(type.getValue());
            }
        }
    }

    public void setBlue(int blue) {
        if (type.getValue() != null) {
            synchronized (type) {
                type.getValue().color = Color.argb(255, Color.red(type.getValue().color), Color.green(type.getValue().color), blue);
                type.setValue(type.getValue());
            }
        }
    }
}
