//
// Created by Andre on 2019-06-06.
// Copyright (c) 2019 Vitalii Ratushnyi. All rights reserved.
//

import Foundation
import core_library_common

class MainThreadWorker: Worker {

    func execute(action: Action) {
        if (Thread.isMainThread) {
            action.execute()
        } else {
            DispatchQueue.main.async {
                action.execute()
            }
        }
    }

}
