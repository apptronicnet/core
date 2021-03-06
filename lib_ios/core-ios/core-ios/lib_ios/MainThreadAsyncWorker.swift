//
// Created by Andre on 2019-06-06.
// Copyright (c) 2019 Vitalii Ratushnyi. All rights reserved.
//

import Foundation
import core_library_common

class MainThreadAsyncWorker: Worker {

    func execute(action: Action) {
        DispatchQueue.main.async {
            action.execute()
        }
    }

}
