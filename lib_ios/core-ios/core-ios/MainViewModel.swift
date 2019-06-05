//
//  MainViewModel.swift
//  core-ios
//
//  Created by Vitalii Ratushnyi on 6/3/19.
//  Copyright © 2019 Vitalii Ratushnyi. All rights reserved.
//

import Foundation
import ApptronicNetCore

class MainViewModel: ViewModel {
    override init(context: ViewModelContext) {
        super.init(context: <#T##ViewModelContext#>)
        self.name = value<String>("John")
    }
    
    var name: Property?
    
}
