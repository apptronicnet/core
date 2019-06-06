//
//  MainViewModel.swift
//  core-ios
//
//  Created by Vitalii Ratushnyi on 6/3/19.
//  Copyright © 2019 Vitalii Ratushnyi. All rights reserved.
//

import Foundation
import lib

class A {

    init(value: String) {

    }
}

class B: A {
    var x: String

    override init(value: String) {
        self.x = "123"
        super.init(value: value)
    }
}

class MainViewModel: ViewModel {

    lazy var name: Property = {
        value(defaultValue: "Any")
    }()

    override init(context: ViewModelContext) {
        super.init(context: context)
    }

}
