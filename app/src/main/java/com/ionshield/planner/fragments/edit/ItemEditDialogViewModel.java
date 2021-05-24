package com.ionshield.planner.fragments.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.ItemDao;
import com.ionshield.planner.database.entities.Item;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ItemEditDialogViewModel extends ViewModel {
    private final MutableLiveData<Item> item = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ItemEditDialogViewModel() {
        item.setValue(new Item());
    }

    public void setItemById(long id) {
        ListenableFuture<List<Item>> res = PlannerRepository.getInstance().getItemDao().queryItems(id);
        res.addListener(() -> {
            try {
                List<Item> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (item) {
                        item.postValue(list.get(0));
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writeItem() {
        ItemDao dao = PlannerRepository.getInstance().getItemDao();
        Item value = item.getValue();
        if (value != null) {
            if (value.itemId == 0) {
                dao.insertAll(value);
            }
            else {
                dao.updateAll(value);
            }
        }
    }

    public LiveData<Item> getItem() {
        return item;
    }

    public void setName(String name) {
        if (item.getValue() != null) {
            synchronized (item) {
                item.getValue().name = name;
                item.setValue(item.getValue());
            }
        }
    }

    public void setDescription(String description) {
        if (item.getValue() != null) {
            synchronized (item) {
                item.getValue().description = description;
                item.setValue(item.getValue());
            }
        }
    }

}