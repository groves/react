//
// React - a library for functional-reactive-like programming
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/react/blob/master/LICENSE

#import "RAIntValue.h"
#import "RAIntReactor+Protected.h"

@implementation RAIntValue

- (id)init {
    return [self initWithValue:0];
}

- (id)initWithValue:(int)value {
    if (!(self = [super init])) return nil;
    _value = value;
    return self;
}

- (int)value { return _value; }

- (void)setValue:(int)value {
    if (value == _value) return;
    _value = value;
    [self dispatchEvent:_value];
}

@end
