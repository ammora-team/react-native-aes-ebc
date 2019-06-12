using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace React.Native.Aes.Ebc.RNReactNativeAesEbc
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNReactNativeAesEbcModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNReactNativeAesEbcModule"/>.
        /// </summary>
        internal RNReactNativeAesEbcModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNReactNativeAesEbc";
            }
        }
    }
}
