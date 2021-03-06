/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class XFilter {

    public static UnaryOperator<Map<Device, ? extends Collection<Snapshot>>>
            apply(Optional<? extends Collection<String>> device, Optional<? extends Collection<Integer>> snapshot) {
                System.out.println("d " + device);
                System.out.println("s " + snapshot);
        return device.isPresent() || snapshot.isPresent()
                ? combine(device, snapshot)
                : UserSelector.instance();
    }

    static UnaryOperator<Map<Device, ? extends Collection<Snapshot>>>
            combine(Optional<? extends Collection<String>> device, Optional<? extends Collection<Integer>> snapshot) {
        UnaryOperator<Map<Device, ? extends Collection<Snapshot>>> opDevice = device
                .map(d -> (UnaryOperator<Map<Device, ? extends Collection<Snapshot>>>) new DeviceSelector(d))
                .orElse(UnaryOperator.identity());
        UnaryOperator<Map<Device, ? extends Collection<Snapshot>>> opSnapshot = snapshot
                .map(s -> (UnaryOperator<Map<Device, ? extends Collection<Snapshot>>>) new SnapshotSelector(s))
                .orElse(UnaryOperator.identity());
        return u -> opDevice.andThen(opSnapshot).apply(u);
    }
}
