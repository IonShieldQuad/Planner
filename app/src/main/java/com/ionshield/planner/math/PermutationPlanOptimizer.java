package com.ionshield.planner.math;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.maps.model.LatLng;
import com.ionshield.planner.database.entities.Constraint;
import com.ionshield.planner.database.entities.compound.EventAndItemWithTypesWithLocationsAndNodes;
import com.ionshield.planner.database.entities.compound.ItemWithTypesWithLocationsAndNodes;
import com.ionshield.planner.database.entities.compound.LocationAndNode;
import com.ionshield.planner.database.entities.compound.TypeWithLocationsAndNodes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PermutationPlanOptimizer implements PlanOptimizer {
    private final MutableLiveData<Double> progress = new MutableLiveData<>();
    private final Executor executor = Executors.newFixedThreadPool(4);
    private int runId = 0;

    public static int MAX_PERMUTATIONS = 40000;

    public PermutationPlanOptimizer() {
    }

    @Override
    public Result optimize(LatLng origin, LocalDateTime startTime, List<EventAndItemWithTypesWithLocationsAndNodes> data, List<Constraint> constraints) {
        progress.postValue(null);
        if (data == null) throw new IllegalArgumentException("Data is null");
        Result out = new Result();
        out.origin = origin;
        out.startTime = startTime;

        int total = 1;

        for (int i = 0; i < data.size(); i++) {
            total *= i + 1;
            List<EventWithData> list = getPossibleLocations(data.get(i));
            if (list != null) {
                total *= list.size();
            }
        }

        if (total > MAX_PERMUTATIONS) throw new IllegalArgumentException("Exceeded max permutation count");

        AtomicInteger current = new AtomicInteger(0);
        final Duration[] timeTaken = {null};
        AtomicReference<List<EventWithDateTimeData>> outData = new AtomicReference<>(null);

        int localId;
        synchronized (progress) {
            runId++;
            localId = runId;
        }

        CountDownLatch latch = new CountDownLatch(total);
        int finalTotal = total;
        //List<List<EventWithData>> list
        int count = getPermutation2(new ArrayList<>(), data, seq -> {
            executor.execute(() -> {
                TimeEvaluator.Result timeResult = null;
                boolean ok;
                try {
                    timeResult = TimeEvaluator.evaluate(origin, startTime, seq);
                    ok = ConstraintEvaluator.testConstraints(timeResult.data, constraints);
                }
                catch (IllegalArgumentException e) {
                    ok = false;
                    e.printStackTrace();
                }
                synchronized (progress) {
                    if (localId == runId) {
                        current.incrementAndGet();
                        progress.postValue(current.doubleValue() / finalTotal);
                        latch.countDown();
                    }
                    if (ok && localId == runId) {


                        if (timeTaken[0] == null || timeResult.timeTaken.minus(timeTaken[0]).isNegative()) {
                            timeTaken[0] = timeResult.timeTaken;
                            outData.set(timeResult.data);

                        }
                    }
                }
            });
        });
        System.out.println("Permutations: " + finalTotal);
        assert (/*list.size()*/count == finalTotal);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        out.data = outData.get();
        out.timeTaken = timeTaken[0];

        return out;
    }

    @Override
    public LiveData<Double> getProgress() {
        return progress;
    }

    @NonNull
    private List<List<EventWithData>> getPermutation(List<EventWithData> start, List<EventAndItemWithTypesWithLocationsAndNodes> data, Consumer<List<EventWithData>> forEach) {
        if (data == null || data.isEmpty()) {
            List<List<EventWithData>> res = new ArrayList<>();
            res.add(new ArrayList<>());
            if (start != null && forEach != null) {
                forEach.accept(start);
            }
            return res;
        }

        List<List<EventWithData>> out = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            EventAndItemWithTypesWithLocationsAndNodes thing = data.get(i);

            List<EventAndItemWithTypesWithLocationsAndNodes> recData = new ArrayList<>(data);
            recData.remove(i);

            if (thing == null) return getPermutation(start, recData, forEach);

            List<EventWithData> variants = getPossibleLocations(thing);

            for (EventWithData variant : variants) {
                List<EventWithData> s = new ArrayList<>(start);
                s.add(variant);

                List<List<EventWithData>> permutations = getPermutation(s, recData, forEach);
                for (List<EventWithData> permutation : permutations) {
                    List<EventWithData> list = new ArrayList<>(s);
                    list.addAll(permutation);
                    out.add(list);
                }

            }
        }

        return out;
    }

    private int getPermutation2(List<EventWithData> start, List<EventAndItemWithTypesWithLocationsAndNodes> data, Consumer<List<EventWithData>> forEach) {
        if (data == null || data.isEmpty()) {
            if (start != null && forEach != null) {
                forEach.accept(new ArrayList<>(start));
            }
            return 1;
        }

        int out = 0;
        for (int i = 0; i < data.size(); i++) {
            EventAndItemWithTypesWithLocationsAndNodes thing = data.get(i);

            List<EventAndItemWithTypesWithLocationsAndNodes> recData = new ArrayList<>(data);
            recData.remove(i);

            if (thing == null) return getPermutation2(start, recData, forEach);

            List<EventWithData> variants = getPossibleLocations(thing);

            for (EventWithData variant : variants) {
                List<EventWithData> s = new ArrayList<>(start);
                s.add(variant);

                int count = getPermutation2(s, recData, forEach);
                out += count;

            }
        }

        return out;
    }

    private List<EventWithData> getPossibleLocations(EventAndItemWithTypesWithLocationsAndNodes event) {
        if (event == null) return null;
        List<EventWithData> list = new ArrayList<>();
        ItemWithTypesWithLocationsAndNodes item = event.itemWithTypesWithLocationsAndNodes;
        if (item == null) {
            list.add(new EventWithData(event.event, null));
            return list;
        }

        List<TypeWithLocationsAndNodes> types = item.typesWithLocationsAndNodes;
        for (TypeWithLocationsAndNodes type : types) {
            if (type == null) continue;

            for (LocationAndNode location : type.locationsAndNodes) {
                if (location == null || location.node == null) continue;
                list.add(new EventWithData(event.event, location));
            }

        }

        return list;
    }
}
