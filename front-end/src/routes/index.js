import { lazy } from 'react'

const routes = [
    {
        path: "/",
        component: lazy(() => import("../views/home"))
    },
    {
        path: "/s/:code",
        component: lazy(() => import("../views/s"))
    },
    {
        path: "/openAPI",
        component: lazy(() => import("../views/openAPI"))
    },
    {
        path: "/404/:code",
        component: lazy(() => import("../views/notFound"))
    }
];

export { routes }