package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.operator.Operator;

public class MapTubeStage<UK, UV, DK, DV> extends MapTube<DK, DV> {

    private final Operator<Pair<UK, UV>, Pair<DK, DV>> op;
    private final MapTube<UK, UV> upstream;

    public MapTubeStage(Operator<Pair<UK, UV>, Pair<DK, DV>> op, MapTube<UK, UV> upstream) {
        this.op = op;
        this.upstream = upstream;
    }

    @Override
    public Capsule<Pair<DK, DV>> next() {
        return op.next(upstream);
    }
}