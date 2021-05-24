package com.ionshield.planner.fragments.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.TypeItemRefDao;
import com.ionshield.planner.database.entities.Item;
import com.ionshield.planner.database.entities.Type;
import com.ionshield.planner.database.entities.TypeItemRef;
import com.ionshield.planner.database.entities.compound.RefTypeAndItem;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TypeItemRefEditDialogViewModel extends ViewModel {
    private final MutableLiveData<RefTypeAndItem> ref = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TypeItemRefEditDialogViewModel() {
        RefTypeAndItem r = new RefTypeAndItem();
        r.ref = new TypeItemRef();
        ref.setValue(r);
    }

    public void setTypeItemRefById(long id) {
        ListenableFuture<List<RefTypeAndItem>> res = PlannerRepository.getInstance().getTypeItemRefDao().queryTypeItemRefsById(id);
        res.addListener(() -> {
            try {
                List<RefTypeAndItem> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (ref) {
                        ref.postValue(list.get(0));
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writeRef() {
        TypeItemRefDao dao = PlannerRepository.getInstance().getTypeItemRefDao();
        RefTypeAndItem value = ref.getValue();
        if (value != null) {
            if (value.ref.typeItemRefId == 0) {
                dao.insertAll(value.ref);
            }
            else {
                dao.updateAll(value.ref);
            }
        }
    }

    public LiveData<RefTypeAndItem> getRef() {
        return ref;
    }


    public void setItem(Item item) {
        if (ref.getValue() != null) {
            synchronized (ref) {
                ref.getValue().item = item;
                if (item != null) {
                    ref.getValue().ref.itemId = item.itemId;
                }
                ref.setValue(ref.getValue());
            }
        }
    }

    public void setType(Type type) {
        if (ref.getValue() != null) {
            synchronized (ref) {
                ref.getValue().type = type;
                if (type != null) {
                    ref.getValue().ref.typeId = type.typeId;
                }
                ref.setValue(ref.getValue());
            }
        }
    }

}
