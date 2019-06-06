//
// Created by Andre on 2019-06-06.
// Copyright (c) 2019 Vitalii Ratushnyi. All rights reserved.
//

import os
import Foundation
import lib

class IosPlatform: Platform {

    func logMessage(text: String) {
        print(text)
    }

    func defaultWorkerProvider() -> WorkerProvider {
        return InstanceWorkerProvider(worker: MainThreadWorker())
    }

    func defaultAsyncWorkerProvider() -> WorkerProvider {
        return InstanceWorkerProvider(worker: MainThreadAsyncWorker())
    }

    func uiWorkerProvider() -> WorkerProvider {
        return InstanceWorkerProvider(worker: MainThreadWorker())
    }

    func uiAsyncWorkerProvider() -> WorkerProvider {
        return InstanceWorkerProvider(worker: MainThreadAsyncWorker())
    }

    func runInNewThread(action: @escaping () -> KotlinUnit) {
        DispatchQueue.global(qos: .background).async {
            action()
        }
    }

    func pauseCurrentThread(timeInMillis: Int64) {
        sleep(UInt32(truncatingIfNeeded: timeInMillis))
    }

    func currentTimeInMillis() -> Int64 {
        return Int64(Date().timeIntervalSince1970 * 1000)
    }

    func elapsedRealtimeMillis() -> Int64 {
        return (Int64) (DispatchTime.now().uptimeNanoseconds / 1000000)
    }

}
