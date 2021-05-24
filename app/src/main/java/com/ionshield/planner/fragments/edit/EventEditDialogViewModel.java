package com.ionshield.planner.fragments.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.EventDao;
import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.Item;
import com.ionshield.planner.database.entities.compound.EventAndItem;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventEditDialogViewModel extends ViewModel {
    private final MutableLiveData<EventAndItem> event = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public EventEditDialogViewModel() {
        EventAndItem eventAndItem = new EventAndItem();
        eventAndItem.event = new Event();
        event.setValue(eventAndItem);
    }

    public void setEventById(long id, Long planId) {
        ListenableFuture<List<EventAndItem>> res = PlannerRepository.getInstance().getEventDao().queryEventsAndItems(id);
        res.addListener(() -> {
            try {
                List<EventAndItem> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (event) {
                        if (planId != null) {
                            list.get(0).event.planId = planId;
                        }
                        event.postValue(list.get(0));
                    }
                }
                else {
                    EventAndItem eventAndItem = new EventAndItem();
                    eventAndItem.event = new Event();
                    if (planId != null) {
                        eventAndItem.event.planId = planId;
                    }
                    event.postValue(eventAndItem);

                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writeEvent() {
        EventDao dao = PlannerRepository.getInstance().getEventDao();
        EventAndItem value = event.getValue();
        if (value != null) {
            if (value.event.eventId == 0) {
                dao.insertAll(value.event);
            }
            else {
                dao.updateAll(value.event);
            }
        }
    }

    public LiveData<EventAndItem> getEvent() {
        return event;
    }

    public void setName(String name) {
        if (event.getValue() != null) {
            synchronized (event) {
                event.getValue().event.name = name;
                event.setValue(event.getValue());
            }
        }
    }

    public void setDescription(String description) {
        if (event.getValue() != null) {
            synchronized (event) {
                event.getValue().event.description = description;
                event.setValue(event.getValue());
            }
        }
    }

    public void setItem(Item item) {
        if (event.getValue() != null) {
            synchronized (event) {
                event.getValue().item = item;
                if (item != null) {
                    event.getValue().event.itemId = item.itemId;
                }
                event.setValue(event.getValue());
            }
        }
    }

    public void setEnabled(boolean enabled) {
        if (event.getValue() != null) {
            synchronized (event) {
                event.getValue().event.isDone = !enabled;
                event.setValue(event.getValue());
            }
        }
    }

    public void setFullDateUsed(boolean enabled) {
        if (event.getValue() != null) {
            synchronized (event) {
                event.getValue().event.isDateUsed = enabled;
                event.setValue(event.getValue());
            }
        }
    }


    public void setTimeMin(int hours, int minutes) {
        if (event.getValue() != null) {
            synchronized (event) {
                if (event.getValue().event.datetimeMin == null) {
                    event.getValue().event.datetimeMin = OffsetDateTime.of(LocalDate.now(), LocalTime.of(hours, minutes), OffsetDateTime.now().getOffset());
                }
                else
                {
                    event.getValue().event.datetimeMin = OffsetDateTime.of(event.getValue().event.datetimeMin.toLocalDate(), LocalTime.of(hours, minutes), OffsetDateTime.now().getOffset());
                }
                event.setValue(event.getValue());
            }
        }
    }


    public void setDateMin(int year, int month, int day) {
        if (event.getValue() != null) {
            synchronized (event) {
                if (event.getValue().event.datetimeMin == null) {
                    event.getValue().event.datetimeMin = OffsetDateTime.of(LocalDate.of(year, month, day), LocalTime.now(), OffsetDateTime.now().getOffset());
                }
                else
                {
                    event.getValue().event.datetimeMin = OffsetDateTime.of(LocalDate.of(year, month, day), event.getValue().event.datetimeMin.toLocalTime(), OffsetDateTime.now().getOffset());
                }
                event.setValue(event.getValue());
            }
        }
    }


    public void setTimeMax(int hours, int minutes) {
        if (event.getValue() != null) {
            synchronized (event) {
                if (event.getValue().event.datetimeMax == null) {
                    event.getValue().event.datetimeMax = OffsetDateTime.of(LocalDate.now(), LocalTime.of(hours, minutes), OffsetDateTime.now().getOffset());
                }
                else
                {
                    event.getValue().event.datetimeMax = OffsetDateTime.of(event.getValue().event.datetimeMax.toLocalDate(), LocalTime.of(hours, minutes), OffsetDateTime.now().getOffset());
                }
                event.setValue(event.getValue());
            }
        }
    }


    public void setDateMax(int year, int month, int day) {
        if (event.getValue() != null) {
            synchronized (event) {
                if (event.getValue().event.datetimeMax == null) {
                    event.getValue().event.datetimeMax = OffsetDateTime.of(LocalDate.of(year, month, day), LocalTime.now(), OffsetDateTime.now().getOffset());
                }
                else
                {
                    event.getValue().event.datetimeMax = OffsetDateTime.of(LocalDate.of(year, month, day), event.getValue().event.datetimeMax.toLocalTime(), OffsetDateTime.now().getOffset());
                }
                event.setValue(event.getValue());
            }
        }
    }

    public void setDuration(Duration duration) {
        if (event.getValue() != null) {
            synchronized (event) {
                event.getValue().event.duration = duration;
                event.setValue(event.getValue());
            }
        }
    }
}
