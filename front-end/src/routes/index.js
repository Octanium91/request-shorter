import { lazy } from 'react'

const routes = [
    {
        path: "/",
        component: lazy(() => import("../views/home")),
        fullView: true,
        accessRoles: [],
    },
    {
        path: "/s/:code",
        component: lazy(() => import("../views/s")),
        fullView: true,
        accessRoles: [],
    },
    {
        path: "/404/:code",
        component: lazy(() => import("../views/notFound")),
        fullView: true,
        accessRoles: [],
    }
];

export { routes }